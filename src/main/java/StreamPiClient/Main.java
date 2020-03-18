package StreamPiClient;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        dash d = new dash();
        io i = new io();
        String[] conf = i.readFileArranged("config","::");
        Scene s = new Scene(d, Integer.parseInt(conf[0]), Integer.parseInt(conf[1]));
        primaryStage.setScene(s);
        primaryStage.show();
        d.initialize();
    }


    public static void main(String[] args) {
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true" );
        launch(args);
    }
}