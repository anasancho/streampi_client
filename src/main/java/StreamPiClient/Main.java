package StreamPiClient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Scene s = new Scene(new dash());
        primaryStage.setScene(s);
        primaryStage.show();
    }

    enum platform{
        ios, android, raspberrypi, windows
    }

    static platform buildPlatform = platform.windows;

    public static void main(String[] args) {
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true" );
        launch(args);
    }
}
