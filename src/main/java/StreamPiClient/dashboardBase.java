package StreamPiClient;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXToggleButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public abstract class dashboardBase extends StackPane {

    protected StackPane alertStackPane;
    protected VBox actionsVBox;
    protected VBox goodbyePane;
    protected Label label;
    protected VBox loadingPane;
    protected JFXSpinner mainSpinner;
    protected VBox settingsPane;
    protected HBox hBox;
    protected Label label0;
    protected Region region;
    protected JFXButton closeSettingsButton;
    protected ImageView imageView0;
    protected HBox hBox0;
    protected Label label1;
    protected TextField serverIPField;
    protected Label label3;
    protected TextField serverPortField;
    protected HBox hBox2;
    protected Label label5;
    protected JFXToggleButton animationsToggleButton;
    protected Label label6;
    protected JFXToggleButton debugModeToggleButton;
    protected Label currentStatusLabel;
    protected Label unableToConnectReasonLabel;
    protected HBox settingsButtonsBar;
    protected JFXButton applySettingsAndRestartButton;

    public dashboardBase() {
        alertStackPane = new StackPane();
        actionsVBox = new VBox();
        goodbyePane = new VBox();
        label = new Label();
        loadingPane = new VBox();
        loadingPane.setAlignment(Pos.CENTER);
        mainSpinner = new JFXSpinner();
        mainSpinner.setProgress(-1);
        mainSpinner.setPrefSize(50,50);
        settingsPane = new VBox();
        hBox = new HBox();
        label0 = new Label();
        region = new Region();
        closeSettingsButton = new JFXButton();
        imageView0 = new ImageView();
        hBox0 = new HBox();
        label1 = new Label();
        serverIPField = new TextField();
        label3 = new Label();
        serverPortField = new TextField();
        hBox2 = new HBox();
        label5 = new Label();
        animationsToggleButton = new JFXToggleButton();
        label6 = new Label();
        debugModeToggleButton = new JFXToggleButton();
        currentStatusLabel = new Label();
        unableToConnectReasonLabel = new Label();
        settingsButtonsBar = new HBox();
        applySettingsAndRestartButton = new JFXButton();

        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        getStyleClass().add("pane");

        actionsVBox.setAlignment(javafx.geometry.Pos.CENTER);
        VBox.setVgrow(actionsVBox,Priority.ALWAYS);
        actionsVBox.setCache(true);
        actionsVBox.setCacheHint(javafx.scene.CacheHint.SPEED);
        actionsVBox.getStyleClass().add("pane");

        goodbyePane.setAlignment(javafx.geometry.Pos.CENTER);
        goodbyePane.getStyleClass().add("pane");
        goodbyePane.setVisible(false);

        label.setText("Goodbye");

        loadingPane.setCache(true);
        loadingPane.setCacheHint(javafx.scene.CacheHint.SPEED);
        loadingPane.getStyleClass().add("pane");

        settingsPane.setCache(true);
        settingsPane.setCacheHint(javafx.scene.CacheHint.SPEED);
        settingsPane.setSpacing(10.0);
        settingsPane.getStyleClass().add("pane");

        //StackPane.setMargin(this, new Insets(15));
        setPadding(new Insets(15));

        label0.setText("Settings");
        //HBox.setMargin(label0, new Insets(15,0,0,0));
        label0.getStyleClass().add("h3");
        label0.getStyleClass().add("HeaderLabel");

        HBox.setHgrow(region, Priority.ALWAYS);

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

        label1.setText("Server IP");

        serverIPField.setCache(true);
        serverIPField.setCacheHint(javafx.scene.CacheHint.SPEED);

        label3.setText("Port");

        serverPortField.setCache(true);
        serverPortField.setCacheHint(javafx.scene.CacheHint.SPEED);

        hBox2.setSpacing(10.0);

        animationsToggleButton.setCache(true);
        animationsToggleButton.setCacheHint(javafx.scene.CacheHint.SPEED);
        animationsToggleButton.setOnAction(this::animationsToggleButtonClicked);

        label5.setText("Animations");

        label6.setText("Debug Mode");

        debugModeToggleButton.setCache(true);
        debugModeToggleButton.setCacheHint(javafx.scene.CacheHint.SPEED);
        debugModeToggleButton.setOnAction(this::debugModeToggleButtonClicked);

        currentStatusLabel.setText("Current Status : NOT CONNECTED");

        unableToConnectReasonLabel.setText("Unable to Connect (<Localised Message>)");

        settingsButtonsBar.setAlignment(javafx.geometry.Pos.CENTER);
        settingsButtonsBar.setSpacing(25.0);

        applySettingsAndRestartButton.setCache(true);
        applySettingsAndRestartButton.setCacheHint(javafx.scene.CacheHint.SPEED);
        applySettingsAndRestartButton.setOnAction(this::applySettingsAndRestartButtonClicked);
        applySettingsAndRestartButton.setTextFill(Color.GREEN);
        applySettingsAndRestartButton.setText("Apply Settings And Restart");

        setOnMouseDragged(event->openSettings());

        getChildren().add(alertStackPane);
        getChildren().add(actionsVBox);
        goodbyePane.getChildren().add(label);
        getChildren().add(goodbyePane);
        loadingPane.getChildren().add(mainSpinner);
        getChildren().add(loadingPane);
        hBox.getChildren().add(label0);
        hBox.getChildren().add(region);
        hBox.getChildren().add(closeSettingsButton);
        settingsPane.getChildren().add(hBox);

        Region rx = new Region();
        HBox.setHgrow(rx, Priority.ALWAYS);
        hBox0.getChildren().addAll(label1, serverIPField, rx, label3, serverPortField);
        hBox0.setAlignment(Pos.CENTER_LEFT);
        settingsPane.getChildren().add(hBox0);


        Region rw = new Region();
        HBox.setHgrow(rw, Priority.ALWAYS);

        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox2.getChildren().addAll(label5, animationsToggleButton, rw, label6, debugModeToggleButton);

        settingsPane.getChildren().add(hBox2);
        settingsPane.getChildren().add(currentStatusLabel);
        settingsPane.getChildren().add(unableToConnectReasonLabel);
        settingsButtonsBar.getChildren().add(applySettingsAndRestartButton);
        settingsPane.getChildren().add(settingsButtonsBar);
        getChildren().add(settingsPane);

        loadingPane.toFront();

        settingsPane.setOpacity(0);
        actionsVBox.setOpacity(0);
    }

    protected abstract void returnToParentLayerButtonClicked(javafx.event.ActionEvent actionEvent);

    protected abstract void openSettings();

    protected abstract void closeSettingsButtonClicked(javafx.event.ActionEvent actionEvent);

    protected abstract void animationsToggleButtonClicked(javafx.event.ActionEvent actionEvent);

    protected abstract void debugModeToggleButtonClicked(javafx.event.ActionEvent actionEvent);

    protected abstract void applySettingsAndRestartButtonClicked(javafx.event.ActionEvent actionEvent);

}
