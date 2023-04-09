package lk.crewx.pos.util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigation {
    private static Stage stage;
    private static AnchorPane pane;

    public static void navigate(Routes routes, AnchorPane anchorPane) throws IOException {
        switch (routes) {
            case ITEMS:
                setPanel("ItemWindow", anchorPane);
            case CATEGORY:
                setPanel("CategoryWindow", anchorPane);
        }

    }

    private static void setPanel(String s, AnchorPane anchorPane) throws IOException {
        Parent parent = FXMLLoader.load((Navigation.class.getResource("/view/" + s + ".fxml")));
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(parent);
    }

    public static void navigate(Routes routes, Event event) throws IOException {
        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();
        stage.close();
        switch (routes) {
            case DASHBOARD:
                initUI("Dashboard");
                break;
        }
    }

    public static void initUI(String location) throws IOException {
        Scene scene = null;
        scene = new Scene(FXMLLoader.load(Navigation.class.getResource("/view/" + location + ".fxml")));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
