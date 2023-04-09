package lk.crewx.pos.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.crewx.pos.db.BalanceUpdating;
import lk.crewx.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CashierMoneyUpdateController {
    public Label lblOnHand;
    public TextField txtAmount;

    public void initialize() {
        try {
            ResultSet res = CrudUtil.execute("SELECT balance FROM money WHERE id=0");
            if (res.next()) {
                lblOnHand.setText(String.valueOf(res.getDouble(1)));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void withdrawMoneyOnAction(ActionEvent actionEvent) {
        double amount = Double.parseDouble(txtAmount.getText());
        boolean b = BalanceUpdating.removeBaance(amount);
        if (b) {
            new Alert(Alert.AlertType.CONFIRMATION, "Done").show();
            Stage stage =(Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.hide();
        } else {
            new Alert(Alert.AlertType.ERROR, "Error").show();
        }
    }
}