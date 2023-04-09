package lk.crewx.pos.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lk.crewx.pos.db.BalanceUpdating;
import lk.crewx.pos.db.DBConnection;
import lk.crewx.pos.model.Suppliers;
import lk.crewx.pos.util.CrudUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class PaySupplierController {
    public Label lblSupplierID;
    public Label lblDate;
    public JFXTextField txtPayment;

    public void setData(Suppliers s) {
        lblSupplierID.setText(String.valueOf(s.getId()));
        lblDate.setText(String.valueOf(LocalDate.now()));
    }

    public void payOnAction(ActionEvent actionEvent) throws SQLException {
        double amount = Double.parseDouble(txtPayment.getText());
        int supID = Integer.parseInt(lblSupplierID.getText());
        LocalDate date = LocalDate.parse(lblDate.getText());
        Connection con = null;

        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            boolean added = CrudUtil.execute("INSERT INTO supplier_transactions(date,amount,supID) VALUES(?,?,?)", date, amount, supID);
            if (added) {
                boolean b = BalanceUpdating.removeBaance(amount);
                if (b) {
                    con.commit();
                    new Alert(Alert.AlertType.CONFIRMATION, "Successfully paid to supplier").show();
                    Stage stage =(Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.hide();
                } else {
                    con.rollback();
                    new Alert(Alert.AlertType.ERROR, "Error While Updating").show();
                }
            } else {
                con.rollback();
                new Alert(Alert.AlertType.ERROR, "Error While Updating").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            con.setAutoCommit(true);
        }

    }
}
