package lk.crewx.pos.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lk.crewx.pos.util.CrudUtil;
import lk.crewx.pos.util.Navigation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsWindowController {

    public JFXButton btnMonthlyIncome;
    public JFXButton btnDailyIncome;
    public Label lblCashierHand;

    public void initialize() {
        loadBalance();
    }

    private void loadBalance() {
        try {
            ResultSet res = CrudUtil.execute("SELECT balance FROM money WHERE id=0");
            if (res.next()) {
                lblCashierHand.setText(String.valueOf(res.getDouble(1)));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void viewMonthlyIncomeOnAction(ActionEvent actionEvent) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(Navigation.class.getResource("/view/MonthlyReport.fxml")));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void viewDailyIncomeOnAction(ActionEvent actionEvent) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(Navigation.class.getResource("/view/DailyReport.fxml")));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void viewEditMoney(ActionEvent actionEvent) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(Navigation.class.getResource("/view/CashierMoneyUpdate.fxml")));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
