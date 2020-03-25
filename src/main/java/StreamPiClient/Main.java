package StreamPiClient;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
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

    static final boolean isMobile = false;

    public static void main(String[] args) {
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true" );
        launch(args);
    }
}
