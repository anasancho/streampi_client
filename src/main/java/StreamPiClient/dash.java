package StreamPiClient;

import animatefx.animation.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class dash extends dashboardBase {

    int maxActionsPerRow;
    int maxNoOfRows;
    String[][] actions;
    String thisDeviceIP;

    String serverIP;
    int serverPort;
    boolean isConnected = false;
    String separator = "::";
    Image doneIcon = new Image(getClass().getResourceAsStream("done.png"));
    Image failedIcon = new Image(getClass().getResourceAsStream("failed.png"));

    boolean debugMode = false;

    int eachActionSize;
    int eachActionPadding;

    public HashMap<String, Object> config;

    public io io;

    void readConfig() throws Exception
    {
        config = new HashMap<>();
        String[] configArray = io.readFileArranged("config","::");
        if(Main.isMobile)
        {
            config.put("width",(int) getWidth());
            config.put("height",(int) getHeight());
        }
        else
        {
            config.put("width", Integer.parseInt(configArray[0]));
            config.put("height", Integer.parseInt(configArray[1]));
        }


        config.put("server_ip",configArray[2]);
        serverIP = configArray[2];
        config.put("server_port",Integer.parseInt(configArray[3]));
        serverPort = (int) config.get("server_port");
        config.put("device_nick_name",configArray[4]);
        config.put("animations_mode",configArray[5]);
        config.put("debug_mode",configArray[6]);
        config.put("each_action_size",Integer.parseInt(configArray[7]));
        config.put("each_action_padding",Integer.parseInt(configArray[8]));
    }


    public void initialize() {
        try
        {
            io = new io();
            readConfig();
            eachActionSize = (int) config.get("each_action_size");
            eachActionPadding = (int) config.get("each_action_padding");
            actionsVBox.setSpacing(eachActionPadding);
            actionsVBox.setPadding(new Insets(3));

            serverIPField.setText(serverIP);
            serverPortField.setText(serverPort+"");
            unableToConnectReasonLabel.setText("");

            if (config.get("animations_mode").equals("0")) {
                animationsToggleButton.setSelected(false);
            } else {
                animationsToggleButton.setSelected(true);
            }

            if (config.get("debug_mode").equals("0")) {
                debugMode = false;
                debugModeToggleButton.setSelected(false);

            } else {
                debugMode = true;
                debugModeToggleButton.setSelected(true);
            }

            actionsVBox.setOnSwipeRight(event -> returnToParentLayerButtonClicked(null));

            System.out.println(eachActionSize + eachActionPadding);
            System.out.println("XXXXX : "+config.get("width")+","+config.get("height"));
            maxActionsPerRow = (((int)config.get("width")) / (eachActionSize + eachActionPadding));
            maxNoOfRows = (((int)config.get("height")) / (eachActionSize + eachActionPadding));


            socketCommThread = new Thread(socketCommTask);
            socketCommThread.setDaemon(true);
            socketCommThread.start();

            checkServerConnection();
        }
        catch (Exception e)
        {
            showErrorAlert("Permissions","Please provide Storage permissions to StreamPi!", true);
            e.printStackTrace();
        }


    }

    @Override
    protected void animationsToggleButtonClicked(ActionEvent event) {
        if (animationsToggleButton.isSelected()) {
            updateConfig("animations_mode", "1");
        } else {
            updateConfig("animations_mode", "0");
        }
    }

    @Override
    protected void debugModeToggleButtonClicked(ActionEvent event) {
        if (debugModeToggleButton.isSelected()) {
            updateConfig("debug_mode", "1");
        } else {
            updateConfig("debug_mode", "0");
        }
    }


    pane currentPane = pane.loading;
    public void switchPane(pane p)
    {
        if(p!=currentPane)
        {
            if (config.get("animations_mode").equals("0"))
            {
                if(p == pane.loading)
                {
                    Platform.runLater(()->{
                        loadingPane.setOpacity(1);
                        loadingPane.toFront();
                    });
                }
                else if(p== pane.settings)
                {
                    if (!isConnected)
                    {
                        closeSettingsButton.setVisible(false);
                    }
                    else
                    {
                        closeSettingsButton.setVisible(true);
                    }
                    Platform.runLater(()->{
                        settingsPane.setOpacity(1);
                        settingsPane.toFront();
                    });
                }
                else if(p== pane.actions)
                {
                    Platform.runLater(()->{
                        actionsVBox.setOpacity(1);
                        actionsVBox.toFront();
                    });
                }
            }
            else
            {
                if(p== pane.loading)
                {
                    new FadeIn(loadingPane).play();
                    Platform.runLater(loadingPane::toFront);
                }
                else if(p== pane.actions)
                {
                    if(currentPane == pane.settings)
                    {
                        System.out.println("CCA");
                        FadeOutDown s = new FadeOutDown(settingsPane);
                        s.setSpeed(1.5);
                        s.setOnFinished(event -> {
                            actionsVBox.toFront();
                            Platform.runLater(actionsVBox::toFront);
                            settingsPane.toBack();
                        });
                        s.play();
                    }
                    else
                    {
                        Platform.runLater(actionsVBox::toFront);
                        new FadeIn(actionsVBox).play();
                    }
                }
                else if(p== pane.settings)
                {
                    if (!isConnected) {
                        closeSettingsButton.setVisible(false);
                    } else {
                        closeSettingsButton.setVisible(true);
                    }
                    System.out.println("Asdx");
                    Platform.runLater(()->settingsPane.setOpacity(0));
                    FadeInUp z = new FadeInUp(settingsPane);
                    z.setSpeed(1.2);
                    z.setDelay(Duration.millis(50));
                    z.play();
                    Platform.runLater(settingsPane::toFront);
                }
            }

            if(currentPane==pane.loading) loadingPane.setOpacity(0);
            currentPane = p;
        }
    }

    enum pane{
        loading, settings, actions
    }

    //-Dcom.sun.javafx.isEmbedded=true -Dcom.sun.javafx.touch=true -Dcom.sun.javafx.virtualKeyboard=javafx
    Socket s;
    DataInputStream is;
    DataOutputStream os;
    int serverPortTemp = 23;
    Thread socketCommThread;
    boolean isWorking = false;

    public void checkServerConnection() {
        Thread x = new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                if (isWorking)
                    return null;

                isWorking = true;

                String serverIPTemp = serverIPField.getText();

                try {
                    thisDeviceIP = Inet4Address.getLocalHost().getHostAddress();

                    switchPane(pane.loading);

                    if (isConnected) {
                        writeToOS("client_quit::");
                        isConnected = false;
                        Thread.sleep(500);
                        s.close();
                        Platform.runLater(() -> actionsVBox.getChildren().clear());
                        Thread.sleep(3000);
                    }

                    try {
                        serverPortTemp = Integer.parseInt(serverPortField.getText());
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            unableToConnectReasonLabel.setText("Please enter a valid Server Port!");
                            switchPane(pane.settings);
                        });
                        return null;
                    }

                    Platform.runLater(() -> actionsVBox.getChildren().clear());

                    s = new Socket();
                    s.connect(new InetSocketAddress(serverIPTemp, serverPortTemp), 2500);
                    is = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                    os = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));


                    updateConfig("server_ip", serverIPTemp);
                    updateConfig("server_port", serverPortField.getText());
                    currentStatusLabel.setTextFill(Paint.valueOf("#008000"));
                    Platform.runLater(() -> {
                        currentStatusLabel.setText("Current Status :  CONNECTED to " + serverIPTemp + ":" + serverPortTemp);
                        unableToConnectReasonLabel.setText("");
                    });

                    //isUpdateStuff=true;
                    loadActions();
                    isConnected = true;

                    uniByteLen = 0;
                } catch (Exception e) {
                    System.out.println("CFBBBBB");
                    currentStatusLabel.setTextFill(Paint.valueOf("#FF0000"));
                    Platform.runLater(() -> {
                        currentStatusLabel.setText("Current Status :  FAILED TO CONNECT to " + serverIPTemp + ":" + serverPortTemp);
                        unableToConnectReasonLabel.setText(e.getLocalizedMessage());
                    });
                    if (debugMode)
                        e.printStackTrace();
                    isConnected = false;
                    try {
                        Thread.sleep(1500);
                    } catch (Exception e1) {
                        e.printStackTrace();
                    }
                    switchPane(pane.settings);
                }
                isWorking = false;
                return null;
            }
        });

        x.setPriority(2);
        x.start();
    }


    @Override
    protected void applySettingsAndRestartButtonClicked(ActionEvent event) {
        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    String portVal = serverPortField.getText();
                    String ipVal = serverIPField.getText();

                    if (!config.get("server_ip").equals(ipVal) || !config.get("server_port").equals(portVal) || !isConnected) {
                        checkServerConnection();
                    }

                } catch (Exception e) {
                    showErrorAlert("Alert", "Please make sure screen dimensions are valid.", false);
                }
                return null;
            }
        }).start();
    }

    public void showErrorAlert(String heading, String content, boolean noButton) {
        System.out.println("\n\nALERT\nHEADING : "+heading+"\nCONTENT:"+content+"\n\n");
        System.out.println("XD");
        JFXDialogLayout l = new JFXDialogLayout();
        l.getStyleClass().add("dialog_style");
        Label headingLabel = new Label(heading);
        headingLabel.setTextFill(Color.WHITE);
        headingLabel.setFont(Font.font("Roboto Regular", 25));
        l.setHeading(headingLabel);
        Label contentLabel = new Label(content);
        contentLabel.setFont(Font.font("Roboto Regular", 15));
        contentLabel.setTextFill(Color.WHITE);
        contentLabel.setWrapText(true);
        l.setBody(contentLabel);

        JFXDialog alertDialog = new JFXDialog(alertStackPane, l, JFXDialog.DialogTransition.CENTER);
        alertDialog.setCache(true);
        alertDialog.setCacheHint(CacheHint.SPEED);
        alertDialog.setOverlayClose(false);
        alertDialog.getStyleClass().add("dialog_box");

        if(!noButton)
        {
            JFXButton okButton = new JFXButton("OK");
            okButton.setTextFill(Color.WHITE);
            l.setActions(okButton);
            okButton.setOnMouseClicked(event -> {
                alertDialog.close();
                alertDialog.setOnDialogClosed(event1 -> alertStackPane.toBack());
            });
        }


        alertStackPane.toFront();
        alertDialog.show();
    }

    public void updateConfig(String keyName, String newValue){
        try {
            config.put(keyName, newValue);
            String toBeWritten = config.get("width")+ separator + config.get("height") +separator +  config.get("server_ip") + separator + config.get("server_port") + separator + config.get("device_nick_name") + separator + config.get("animations_mode") + separator + config.get("debug_mode") + separator + config.get("each_action_size") + separator + config.get("each_action_padding") + separator;
            io.writeToFile(toBeWritten, "config");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    final String CLIENT_VERSION = "0.0.5";
    Task socketCommTask = new Task<Void>() {
        @Override
        protected Void call() {
            while (true) {
                try {
                    if (isConnected) {

                        String responseFromServerRaw = readFromIS();
                        System.out.println("RFS : " + responseFromServerRaw);
                        String[] response = responseFromServerRaw.split(separator);
                        String msgHeading = response[0];
                        if (msgHeading.equals("client_details")) {
                            Thread.sleep(1000);
                            writeToOS("client_details" + separator + thisDeviceIP + separator + config.get("device_nick_name") + separator + config.get("width") + separator + config.get("height") + separator + maxActionsPerRow + separator + maxNoOfRows + separator + eachActionSize + separator + eachActionPadding + separator + CLIENT_VERSION + separator);
                            //client_details::<deviceIP>::<nick_name>::<device_width>::<device_height>::<max_actions_per_row>::<max_no_of_rows>::
                            // <maxcols>
                        } else if (msgHeading.equals("action_success_response")) {
                            System.out.println("RECEIVEDXXX");
                            new Thread(new Task<Void>() {
                                @Override
                                protected Void call() {
                                    try {
                                        String uniqueID = response[1];
                                        String status = response[2];

                                        showSuccessToUI(uniqueID, status);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                            }).start();
                        } else if (msgHeading.equals("client_action_size_padding_update")) {
                            String newActionSizeString = response[1];
                            String newActionPaddingString = response[2];

                            if (!(eachActionSize + "").equals(newActionSizeString) || !(eachActionPadding + "").equals(newActionPaddingString)) {
                                eachActionPadding = Integer.parseInt(newActionPaddingString);
                                eachActionSize = Integer.parseInt(newActionSizeString);
                                updateConfig("each_action_size", newActionSizeString);
                                updateConfig("each_action_padding", newActionPaddingString);

                                System.out.println("XXXXX : "+config.get("width")+","+config.get("height"));
                                maxActionsPerRow = (((int)config.get("width")) / (eachActionSize + eachActionPadding));
                                maxNoOfRows = (((int)config.get("height")) / (eachActionSize + eachActionPadding));

                                drawLayer(0,-1);
                            }
                        } else if (msgHeading.equals("delete_action")) {
                            System.out.println("Deleting...");
                            io.deleteFile("actions/details/" + response[1]);
                            System.out.println("actions/icons/" + response[2]);
                            io.deleteFile("actions/icons/" + response[2]);
                            loadActions();
                            drawLayer(0,-1);
                        } else if (msgHeading.equals("actions_update")) {
                            //delete all details...
                            for (String[] eachAction : actions) {
                                io.deleteFile("actions/details/" + eachAction[0]);
                            }
                            //System.out.println("sd213123");
                            int noOfActions = Integer.parseInt(response[1]);
                            int currentIndex = 2;
                            for (int i = 0; i < noOfActions; i++) {
                                String[] newAction = response[currentIndex].split("__");
                                String actionID = newAction[0];
                                String actionCasualName = newAction[1];
                                String actionType = newAction[2];
                                String actionContent = newAction[3];
                                String actionIconFileName = newAction[4];
                                String actionRowNo = newAction[5];
                                String actionColNo = newAction[6];
                                String actionLayerIndex = newAction[7];

                                io.writeToFile(actionCasualName + separator + actionType + separator + actionContent + separator + actionIconFileName + separator + actionRowNo + separator + actionColNo + separator + actionLayerIndex + separator, "actions/details/" + actionID);
                                //io.writeToFile(actionCasualName+separator+actionType+separator+actionContent+separator+actionID+separator+actionImageFileName+separator+actionRowNo+separator+actionColNo,"actions/details/"+actionID);
                                currentIndex++;
                            }
                            //System.out.println("updated!");
                            loadActions();
                            drawLayer(0,-1);
                        } else if (msgHeading.equals("update_icon")) {
                            String iconName = response[1];
                            String actionImageBase64 = response[2];

                            byte[] img = Base64.getDecoder().decode(actionImageBase64);
                            io.writeToFileRaw(img, "actions/icons/" + iconName);
                            loadActions();
                            drawLayer(0,-1);
                        } else if (msgHeading.equals("get_actions")) {
                            //System.out.println("145455");
                            String towrite = "client_actions" + separator + actions.length + separator;

                            for (String[] eachAction : actions) {
                                //FileInputStream fs = new FileInputStream("actions/icons/"+eachAction[3]);
                                //byte[] imageB = fs.readAllBytes();
                                //fs.close();
                                //String base64Image = Base64.getEncoder().encodeToString(imageB);
                                towrite += eachAction[0] + "__" + eachAction[1] + "__" + eachAction[2] + "__" + eachAction[3] + "__" + eachAction[4] + "__" + eachAction[5] + "__" + eachAction[6] + "__" + eachAction[7] + separator;
                            }
                            writeToOS(towrite + maxLayers + separator);
                            iconsSent.clear();
                        } else if (msgHeading.equals("client_actions_icons_get")) {
                            for (String[] eachAction : actions) {
                                if (!iconsSent.contains(eachAction[4])) {
                                    iconsSent.add(eachAction[4]);
                                    byte[] imageB = io.returnBytesFromFile("actions/icons/" + eachAction[4]);
                                    String base64Image = Base64.getEncoder().encodeToString(imageB);
                                    System.out.println(eachAction[4] + "GAYFAG");
                                    writeToOS("action_icon::" + eachAction[4] + "::" + base64Image + "::");
                                    Thread.sleep(300);
                                }
                            }
                            drawLayer(0,-1);
                            switchPane(pane.actions);
                        }
                    }
                    Thread.sleep(100);

                } catch (Exception e) {
                    if (!isShutdown) {
                        checkServerConnection();
                        if (debugMode)
                            e.printStackTrace();
                    }
                }
            }
        }
    };

    private void showSuccessToUI(String uniqueID, String status) throws Exception {
        for (Node eachNode : actionsVBox.getChildren()) {
            HBox eachRow = (HBox) eachNode;
            for (Node eachActionPane : eachRow.getChildren()) {
                Pane eachAction = (Pane) eachActionPane;
                String[] xxa = eachAction.getId().split("::");
                if (xxa[2].equals(uniqueID)) {
                    if (!xxa[0].equals("folder")) {
                        Pane iconPane = (Pane) eachAction.getChildren().get(1);
                        ImageView icon = (ImageView) iconPane.getChildren().get(0);

                        if (status.equals("1")) {
                            icon.setImage(doneIcon);
                        } else if (status.equals("0")) {
                            icon.setImage(failedIcon);
                        }

                                                            /*ScaleTransition lol2 = new ScaleTransition(Duration.millis(450), eachAction);
                                                            lol2.setFromX(0.9);
                                                            lol2.setFromY(0.9);
                                                            lol2.setToX(1.0);
                                                            lol2.setToY(1.0);

                                                            FadeTransition lol3 = new FadeTransition(Duration.millis(450),eachAction);
                                                            lol3.setFromValue(0.7);
                                                            lol3.setToValue(1.0);

                                                            lol2.play();
                                                            lol3.play();*/

                        Platform.runLater(() -> eachAction.setDisable(false));
                        FadeIn lol = new FadeIn(iconPane);
                        lol.setSpeed(2.0);
                        lol.setOnFinished(event -> {
                            FadeOut lol2 = new FadeOut(iconPane);
                            lol2.setSpeed(2.0);
                            lol2.setDelay(Duration.millis(200));
                            lol2.play();
                            lol2.setOnFinished(event1 -> {
                                iconPane.setOpacity(0.0);
                                eachAction.setDisable(false);
                            });
                        });
                        lol.setDelay(Duration.millis(100));
                        lol.play();
                        break;
                    } else {
                        System.out.println("FAILED");
                    }
                }
            }
        }
    }

    private void deleteFiles(File file) {
        if (file.isDirectory())
            for (File f : file.listFiles())
                deleteFiles(f);
        else
            file.delete();
    }

    ArrayList<String> iconsSent = new ArrayList<>();

    //Writes to the Output Stream of the Socket connection between pi and pc
    public void writeToOS(String txt) throws Exception {
        byte[] by = txt.getBytes(StandardCharsets.UTF_8);
        os.writeUTF("buff_length::" + by.length + "::");
        os.flush();
        Thread.sleep(500);
        os.write(by);
        os.flush();
        System.out.println("SENT @ " + by.length);
    }

    //Writes from the Input Stream of the Socket connection between pi and pc
    int uniByteLen = 0;

    public String readFromIS() {
        try {
            String bg = is.readUTF();
            byte[] str = new byte[uniByteLen];
            if (bg.startsWith("buff_length")) {
                uniByteLen = Integer.parseInt(bg.split("::")[1]);
                System.out.println("GOT @ " + uniByteLen);
                str = is.readNBytes(uniByteLen);
            }

            if (uniByteLen > 0) {
                uniByteLen = 0;
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    int currentLayer = 0;

    boolean isDisplayMismatchError = false;

    public void drawLayer(int layer, int mode) {
        HBox[] rows = new HBox[maxNoOfRows];

        for (int j = 0; j < maxNoOfRows; j++) {
            rows[j] = new HBox();
            rows[j].setSpacing(eachActionPadding);
            rows[j].setAlignment(Pos.CENTER);

            Pane[] actionPane = new Pane[maxActionsPerRow];
            for (int k = 0; k < maxActionsPerRow; k++) {
                actionPane[k] = new Pane();
                actionPane[k].setPrefSize(eachActionSize, eachActionSize);
                actionPane[k].getStyleClass().add("action_box");
                actionPane[k].setId("nut::nut::nut::");
                //actionPane[k].setStyle("-fx-effect: dropshadow(three-pass-box, red, 5, 0, 0, 0);-fx-background-color:"+config.get("bg_colour"));
            }

            rows[j].getChildren().addAll(actionPane);
        }

        for (String[] eachActionDetails : actions) {
            if (Integer.parseInt(eachActionDetails[7]) != layer)
                continue;

            //System.out.println("actions/icons/"+eachActionDetails[3]);
            ImageView icon = new ImageView(io.returnImage("actions/icons/" + eachActionDetails[4]));
            icon.setFitHeight(eachActionSize);
            icon.setPreserveRatio(false);
            icon.setFitWidth(eachActionSize);

            ImageView resultImgView = new ImageView();
            resultImgView.setPreserveRatio(false);
            resultImgView.setFitWidth(eachActionSize);
            resultImgView.setFitHeight(eachActionSize);

            Pane anotherPane = new Pane(resultImgView);
            anotherPane.setOpacity(0);
            anotherPane.setStyle("-fx-background-color:black;");
            anotherPane.setCache(true);
            anotherPane.setCacheHint(CacheHint.SPEED);

            Pane actionPane = new Pane(icon, anotherPane);
            actionPane.setPrefSize(eachActionSize, eachActionSize);
            actionPane.setPrefSize(eachActionSize, eachActionSize);
            //actionPane.getStyleClass().add("action_box");
            //actionPane.setStyle("-fx-effect: dropshadow(three-pass-box, "+eachActionDetails[4]+", 5, 0, 0, 0);-fx-background-color:"+config.get("bg_colour"));
            actionPane.setId(eachActionDetails[2] + separator + eachActionDetails[3] + separator + eachActionDetails[0] + separator);
                /*actionPane.setOnTouchStationary(new EventHandler<TouchEvent>() {
                    @Override
                    public void handle(TouchEvent event) {
                        allocatedActionMouseEventHandler((Node) event.getSource());
                    }
                });*/
            actionPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    allocatedActionMouseEventHandler((Node) event.getSource());
                }
            });


            int rowNo = Integer.parseInt(eachActionDetails[5]);
            int colNo = Integer.parseInt(eachActionDetails[6]);
            try {
                rows[rowNo].getChildren().set(colNo, actionPane);
            } catch (IndexOutOfBoundsException e) {
                isDisplayMismatchError = true;
            }
        }

        Platform.runLater(() -> {

            settingsPane.setVisible(false);
            loadingPane.setVisible(false);

            if (mode == 0) {
                FadeOutRight gay = new FadeOutRight(actionsVBox);
                gay.setSpeed(3.0);
                gay.play();
                gay.setOnFinished(event -> {
                    actionsVBox.getChildren().clear();
                    actionsVBox.getChildren().addAll(rows);
                    FadeInLeft fag = new FadeInLeft(actionsVBox);
                    fag.setSpeed(2.0);
                    fag.play();
                    fag.setOnFinished(event1->{
                        settingsPane.setVisible(true);
                        loadingPane.setVisible(true);
                    });
                });
            } else if (mode == 1) {
                FadeOutLeft gay = new FadeOutLeft(actionsVBox);
                gay.setSpeed(3.0);
                gay.play();
                gay.setOnFinished(event -> {
                    actionsVBox.getChildren().clear();
                    actionsVBox.getChildren().addAll(rows);
                    FadeInRight fag = new FadeInRight(actionsVBox);
                    fag.setSpeed(2.0);
                    fag.play();
                    fag.setOnFinished(event1->{
                        settingsPane.setVisible(true);
                        loadingPane.setVisible(true);
                    });
                });
            } else {
                actionsVBox.getChildren().clear();
                actionsVBox.getChildren().addAll(rows);

                settingsPane.setVisible(true);
                loadingPane.setVisible(true);
            }


            if (layer != -1)
                currentLayer = layer;

        });
    }

    private void allocatedActionMouseEventHandler(Node n) {
        /*ScaleTransition lol2 = new ScaleTransition(Duration.millis(550), n);
        lol2.setFromX(1.0);
        lol2.setFromY(1.0);
        lol2.setToX(0.9);
        lol2.setToY(0.9);

        FadeTransition lol3 = new FadeTransition(Duration.millis(550), n);
        lol3.setFromValue(1.0);
        lol3.setToValue(0.7);

        lol2.play();
        lol3.play();*/

        sendAction(n.getId());

        //Pane fuck = (Pane) n;

        //String[] splitz = n.getId().split("::");


    }

    @Override
    protected void returnToParentLayerButtonClicked(ActionEvent event) {
        for (String[] eachAction : actions) {
            if (eachAction[2].equals("folder") && eachAction[3].equals(currentLayer + "")) {
                drawLayer(Integer.parseInt(eachAction[7]), 0);
            }
        }
    }

    int maxLayers = 0;

    public void loadActions() throws Exception {

        maxLayers = 0;
        String[] allActionFiles = io.listFiles("actions/details");

        actionsVBox.setAlignment(Pos.TOP_LEFT);

        actions = new String[allActionFiles.length][8];

        int i = 0;
        int lowLayer = 0;
        for (String eachActionFile : allActionFiles) {
            String[] contentArray = io.readFileArranged("actions/details/" + eachActionFile, separator);
            System.out.println(io.readFileRaw("actions/details/" + eachActionFile));
            actions[i][0] = eachActionFile; //Action Unique ID
            actions[i][1] = contentArray[0]; //Casual Name
            actions[i][2] = contentArray[1]; //Action Type
            actions[i][3] = contentArray[2]; //Action Content
            actions[i][4] = contentArray[3]; //Icon
            //System.out.println("iconXX : "+actions[i][3]);
            //actions[i][4] = contentArray[3]; //Ambient Colour
            actions[i][5] = contentArray[4]; //Row No
            actions[i][6] = contentArray[5]; //Column No
            actions[i][7] = contentArray[6]; //Layer
            if (Integer.parseInt(contentArray[6]) > lowLayer)
                lowLayer = Integer.parseInt(contentArray[6]);
            i++;
        }

        maxLayers = lowLayer;

        if(actions.length == 0)
        {
            drawLayer(0,-1);
            switchPane(pane.actions);
        }
    }

    public void sendAction(String rawActionContent) {
        //System.out.println("HOTKEY : "+hotkey);
        System.out.println("AXION" + rawActionContent);
        String[] splitz = rawActionContent.split("::");

        if (splitz[0].equals("folder"))
            drawLayer(Integer.parseInt(splitz[1]), 1);
        else if (splitz[0].equals("set_gpio_out")) {
            System.out.println(rawActionContent);
            String[] s = splitz[1].split("<>");
            Runtime r = Runtime.getRuntime();
            try {
                r.exec("sudo gpio -g mode " + s[0] + " out");
                r.exec("sudo gpio -g write " + s[0] + " " + s[1]);
                showSuccessToUI(splitz[2], "1");
            } catch (Exception e) {
                try {
                    showSuccessToUI(splitz[2], "0");
                } catch (Exception e2) {
                    e.printStackTrace();
                }
                e.printStackTrace();
            }
        } else {
            try {
                writeToOS(rawActionContent);
            } catch (Exception e) {
                checkServerConnection();
                if (debugMode)
                    e.printStackTrace();
            }
        }
    }

    @Override
    protected void openSettings()
    {
        switchPane(pane.settings);
    }

    @Override
    protected void closeSettingsButtonClicked(ActionEvent event) {
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (isConnected) {
                    Platform.runLater(() -> {
                        closeSettingsButton.setDisable(true);
                        switchPane(pane.actions);
                        closeSettingsButton.setDisable(false);
                    });
                }
                return null;
            }
        }).start();

    }

    boolean isShutdown = false;

}
