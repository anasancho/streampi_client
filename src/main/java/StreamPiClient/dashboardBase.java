package StreamPiClient;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public abstract class dashboardBase extends StackPane {

    protected final StackPane alertStackPane;
    protected final VBox actionsVBox;
    protected final VBox goodbyePane;
    protected final Label label;
    protected final StackPane loadingPane;
    protected final ImageView imageView;
    protected final VBox settingsPane;
    protected final HBox hBox;
    protected final Label label0;
    protected final Region region;
    protected final Button closeSettingsButton;
    protected final ImageView imageView0;
    protected final HBox hBox0;
    protected final Label label1;
    protected final TextField serverIPField;
    protected final Label label3;
    protected final TextField serverPortField;
    protected final HBox hBox2;
    protected final Label label5;
    protected final ToggleButton animationsToggleButton;
    protected final Label label6;
    protected final ToggleButton debugModeToggleButton;
    protected final Label currentStatusLabel;
    protected final Label unableToConnectReasonLabel;
    protected final HBox settingsButtonsBar;
    protected final Button applySettingsAndRestartButton;

    public dashboardBase() {
        alertStackPane = new StackPane();
        actionsVBox = new VBox();
        goodbyePane = new VBox();
        label = new Label();
        loadingPane = new StackPane();
        imageView = new ImageView();
        settingsPane = new VBox();
        hBox = new HBox();
        label0 = new Label();
        region = new Region();
        closeSettingsButton = new Button();
        imageView0 = new ImageView();
        hBox0 = new HBox();
        label1 = new Label();
        serverIPField = new TextField();
        label3 = new Label();
        serverPortField = new TextField();
        hBox2 = new HBox();
        label5 = new Label();
        animationsToggleButton = new ToggleButton();
        label6 = new Label();
        debugModeToggleButton = new ToggleButton();
        currentStatusLabel = new Label();
        unableToConnectReasonLabel = new Label();
        settingsButtonsBar = new HBox();
        applySettingsAndRestartButton = new Button();

        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        getStyleClass().add("pane");

        actionsVBox.setAlignment(javafx.geometry.Pos.CENTER);
        VBox.setVgrow(actionsVBox,Priority.ALWAYS);
        actionsVBox.setCache(true);
        actionsVBox.setCacheHint(javafx.scene.CacheHint.SPEED);
        actionsVBox.getStyleClass().add("pane");

        goodbyePane.setAlignment(javafx.geometry.Pos.CENTER);
        goodbyePane.setPrefHeight(200.0);
        goodbyePane.setPrefWidth(100.0);
        goodbyePane.getStyleClass().add("pane");
        goodbyePane.setVisible(false);

        label.setText("Goodbye");

        loadingPane.setCache(true);
        loadingPane.setCacheHint(javafx.scene.CacheHint.SPEED);
        loadingPane.setPrefHeight(200.0);
        loadingPane.setPrefWidth(100.0);
        loadingPane.getStyleClass().add("pane");

        imageView.setFitHeight(100.0);
        imageView.setFitWidth(100.0);
        imageView.setPickOnBounds(true);
        imageView.setSmooth(false);
        imageView.setImage(new Image(getClass().getResource("loading.gif").toExternalForm()));

        settingsPane.setCache(true);
        settingsPane.setCacheHint(javafx.scene.CacheHint.SPEED);
        settingsPane.setSpacing(10.0);
        settingsPane.getStyleClass().add("pane");

        setPadding(new Insets(15));

        label0.setText("Settings");
        HBox.setMargin(label0, new Insets(15,0,0,0));
        label0.getStyleClass().add("h3");
        label0.getStyleClass().add("HeaderLabel");

        HBox.setHgrow(region, javafx.scene.layout.Priority.SOMETIMES);
        region.setPrefWidth(580.0);

        closeSettingsButton.setCache(true);
        closeSettingsButton.setCacheHint(javafx.scene.CacheHint.SPEED);
        closeSettingsButton.setOnAction(this::closeSettingsButtonClicked);
        closeSettingsButton.setPrefHeight(60.0);
        closeSettingsButton.setPrefWidth(55.0);
        closeSettingsButton.setText(" ");

        imageView0.setFitHeight(35.0);
        imageView0.setFitWidth(48.0);
        imageView0.setPickOnBounds(true);
        imageView0.setPreserveRatio(true);
        imageView0.setImage(new Image(getClass().getResource("cancel.png").toExternalForm()));
        closeSettingsButton.setGraphic(imageView0);

        hBox0.setSpacing(10.0);

        label1.setText("StreamPi Server");

        serverIPField.setCache(true);
        serverIPField.setCacheHint(javafx.scene.CacheHint.SPEED);

        label3.setText("Server Port");

        serverPortField.setCache(true);
        serverPortField.setCacheHint(javafx.scene.CacheHint.SPEED);

        hBox2.setSpacing(10.0);

        label5.setText("Animations");

        animationsToggleButton.setCache(true);
        animationsToggleButton.setCacheHint(javafx.scene.CacheHint.SPEED);
        animationsToggleButton.setOnAction(this::animationsToggleButtonClicked);
        animationsToggleButton.setPrefHeight(55.0);
        animationsToggleButton.setPrefWidth(138.0);
        animationsToggleButton.setText("Animations");

        label6.setPrefHeight(58.0);
        label6.setPrefWidth(119.0);
        label6.setText("Debug Mode");
        HBox.setMargin(label6, new Insets(0.0, 0.0, 0.0, 47.0));

        debugModeToggleButton.setCache(true);
        debugModeToggleButton.setCacheHint(javafx.scene.CacheHint.SPEED);
        debugModeToggleButton.setOnAction(this::debugModeToggleButtonClicked);
        debugModeToggleButton.setPrefHeight(55.0);
        debugModeToggleButton.setPrefWidth(138.0);
        debugModeToggleButton.setText("Debug Mode");

        currentStatusLabel.setText("Current Status : NOT CONNECTED");

        unableToConnectReasonLabel.setText("Unable to Connect (<Localised Message>)");

        settingsButtonsBar.setAlignment(javafx.geometry.Pos.CENTER);
        settingsButtonsBar.setSpacing(25.0);

        applySettingsAndRestartButton.setCache(true);
        applySettingsAndRestartButton.setCacheHint(javafx.scene.CacheHint.SPEED);
        applySettingsAndRestartButton.setOnAction(this::applySettingsAndRestartButtonClicked);
        applySettingsAndRestartButton.setText("Apply Settings And Restart");

        setOnMouseDragged(event->openSettings());

        getChildren().add(alertStackPane);
        getChildren().add(actionsVBox);
        goodbyePane.getChildren().add(label);
        getChildren().add(goodbyePane);
        loadingPane.getChildren().add(imageView);
        getChildren().add(loadingPane);
        hBox.getChildren().add(label0);
        hBox.getChildren().add(region);
        hBox.getChildren().add(closeSettingsButton);
        settingsPane.getChildren().add(hBox);

        Region rx = new Region();
        HBox.setHgrow(rx, Priority.ALWAYS);
        hBox0.getChildren().addAll(label1, serverIPField, rx, label3, serverPortField);

        settingsPane.getChildren().add(hBox0);


        Region rw = new Region();
        HBox.setHgrow(rw, Priority.ALWAYS);

        hBox2.getChildren().addAll(label5, animationsToggleButton, rw, label6, debugModeToggleButton);

        settingsPane.getChildren().add(hBox2);
        settingsPane.getChildren().add(currentStatusLabel);
        settingsPane.getChildren().add(unableToConnectReasonLabel);
        settingsButtonsBar.getChildren().add(applySettingsAndRestartButton);
        settingsPane.getChildren().add(settingsButtonsBar);
        getChildren().add(settingsPane);

        loadingPane.toFront();
    }

    protected abstract void returnToParentLayerButtonClicked(javafx.event.ActionEvent actionEvent);

    protected abstract void openSettings();

    protected abstract void closeSettingsButtonClicked(javafx.event.ActionEvent actionEvent);

    protected abstract void animationsToggleButtonClicked(javafx.event.ActionEvent actionEvent);

    protected abstract void debugModeToggleButtonClicked(javafx.event.ActionEvent actionEvent);

    protected abstract void applySettingsAndRestartButtonClicked(javafx.event.ActionEvent actionEvent);

}
