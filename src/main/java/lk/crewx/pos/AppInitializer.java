package lk.crewx.pos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginWindow.fxml"))));
//        primaryStage.setResizable(false);
//        primaryStage.show();
//        primaryStage.initStyle(StageStyle.UNDECORATED);
//        primaryStage.centerOnScreen();

        URL resource = this.getClass().getResource("/view/LoginWindow.fxml");
        Parent load = FXMLLoader.load(resource);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(load));
        primaryStage.centerOnScreen();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

        Image image = new Image("/assets/1.jpg");
        primaryStage.getIcons().add(image);
        primaryStage.setTitle("PD-Traders");
    }
}
