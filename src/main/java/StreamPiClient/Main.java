package StreamPiClient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        dash d = new dash();
        Scene s = new Scene(d);
        primaryStage.setScene(s);
        primaryStage.show();
        d.initialize();
    }

    enum platform{
        ios, android, aarch64Linux, windows
    }

    static platform buildPlatform = platform.windows;

    public static void main(String[] args) {
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true" );
        launch(args);
    }
}
