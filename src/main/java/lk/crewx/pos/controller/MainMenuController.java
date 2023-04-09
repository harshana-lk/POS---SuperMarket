package lk.crewx.pos.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import lk.crewx.pos.util.Env;
import lk.crewx.pos.util.Mailing;
import lk.crewx.pos.util.Navigation;
import lk.crewx.pos.util.Routes;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainMenuController extends Application {
    public Line lineRepots;
    public Line lineItems;
    public Line lineHistory;
    public Line lineSupplier;
    public Line lineOrder;
    public Line lineHome;
    public JFXButton btnHome;
    public JFXButton btnOrder;
    public JFXButton btnReports;
    public JFXButton btnItems;
    public JFXButton btnHistory;
    public JFXButton btnSupplier;
    public JFXButton btnLogOut;
    public JFXButton btnClose;
    public AnchorPane mainContext;
    public AnchorPane panelContext;
    public AnchorPane dashboardContext;
    public Label lblDate;

    public void initialize() {
        setDate();
        setPermissions();

    }

    private void setPermissions() {
        if (Env.user.equals("staff")) {
            btnHistory.setVisible(false);
            btnItems.setVisible(false);
            btnSupplier.setVisible(false);
            btnReports.setVisible(false);
        }
    }

    public void HomeOnAction() throws IOException {
        lineHome.setOpacity(1.00);
        lineOrder.setOpacity(0.00);
        lineRepots.setOpacity(0.00);
        lineItems.setOpacity(0.00);
        lineHistory.setOpacity(0.00);
        lineSupplier.setOpacity(0.00);

        Parent parent = FXMLLoader.load((getClass().getResource("/view/Home.fxml")));
        dashboardContext.getChildren().clear();
        dashboardContext.getChildren().add(parent);
    }

    public void OrderOnAction(ActionEvent actionEvent) throws IOException {
        lineHome.setOpacity(0.00);
        lineOrder.setOpacity(1.00);
        lineRepots.setOpacity(0.00);
        lineItems.setOpacity(0.00);
        lineHistory.setOpacity(0.00);
        lineSupplier.setOpacity(0.00);

        Parent parent = FXMLLoader.load((getClass().getResource("/view/OrderWindow.fxml")));
        dashboardContext.getChildren().clear();
        dashboardContext.getChildren().add(parent);
    }

    public void ReportsOnAction(ActionEvent actionEvent) throws IOException {
        lineHome.setOpacity(0.00);
        lineOrder.setOpacity(0.00);
        lineRepots.setOpacity(1.00);
        lineItems.setOpacity(0.00);
        lineHistory.setOpacity(0.00);
        lineSupplier.setOpacity(0.00);

        Parent parent = FXMLLoader.load((getClass().getResource("/view/ReportsWindow.fxml")));
        dashboardContext.getChildren().clear();
        dashboardContext.getChildren().add(parent);
    }

    public void ItemsOnAction(ActionEvent actionEvent) throws IOException {
        lineHome.setOpacity(0.00);
        lineOrder.setOpacity(0.00);
        lineRepots.setOpacity(0.00);
        lineItems.setOpacity(1.00);
        lineHistory.setOpacity(0.00);
        lineSupplier.setOpacity(0.00);

        Parent parent = FXMLLoader.load((getClass().getResource("/view/ItemWindow.fxml")));
        dashboardContext.getChildren().clear();
        dashboardContext.getChildren().add(parent);

//        Navigation.navigate(Routes.ITEMS, dashboardContext);
//        dashboardContext.getChildren().clear();
//        dashboardContext.getChildren().add(parent);
    }

    public void HistoryOnAction(ActionEvent actionEvent) throws IOException {
        lineHome.setOpacity(0.00);
        lineOrder.setOpacity(0.00);
        lineRepots.setOpacity(0.00);
        lineItems.setOpacity(0.00);
        lineHistory.setOpacity(1.00);
        lineSupplier.setOpacity(0.00);

        Parent parent = FXMLLoader.load((getClass().getResource("/view/History.fxml")));
        dashboardContext.getChildren().clear();
        dashboardContext.getChildren().add(parent);
    }

    public void SupplierOnAction(ActionEvent actionEvent) throws IOException {
        lineHome.setOpacity(0.00);
        lineOrder.setOpacity(0.00);
        lineRepots.setOpacity(0.00);
        lineItems.setOpacity(0.00);
        lineHistory.setOpacity(0.00);
        lineSupplier.setOpacity(1.00);

        Parent parent = FXMLLoader.load((getClass().getResource("/view/SupplierWindow.fxml")));
        dashboardContext.getChildren().clear();
        dashboardContext.getChildren().add(parent);
    }

    public void LogOutOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        Mailing.startThread();
        Scene scene = new Scene(FXMLLoader.load(Navigation.class.getResource("/view/LoginWindow.fxml")));
        stage.close();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void CloseOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public void outOfStockOnAction(MouseEvent mouseEvent) {
    }

    public void expiredItemsOnAction(MouseEvent mouseEvent) {
    }

    public void trendingItemsOnAction(MouseEvent mouseEvent) {
    }

    public void setDate() {
        try {
            HomeOnAction();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }


}

