package lk.crewx.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.crewx.pos.dao.custom.OrdersDAO;
import lk.crewx.pos.dao.custom.impl.OrdersDAOImpl;
import lk.crewx.pos.db.BalanceUpdating;
import lk.crewx.pos.db.DBConnection;
import lk.crewx.pos.model.Orders;

import java.sql.Connection;
import java.sql.SQLException;

public class LoanPayController {
    public Label lblCustName;
    public Label lblDate;
    public JFXButton btnPay;
    public JFXTextField txtPayment;
    public Label lblOID;
    public Label lblTotalPayable;

    OrdersDAO ordersDAO = new OrdersDAOImpl();

    public void setData(Orders orders) {
        lblCustName.setText(orders.getName());
        lblOID.setText(String.valueOf(orders.getId()));
        lblDate.setText(String.valueOf(orders.getDate()));
        lblTotalPayable.setText(String.valueOf(orders.getToPaid()));
    }

    public void payOnAction(ActionEvent actionEvent) throws SQLException {
        double amount = Double.parseDouble(txtPayment.getText());
        Connection con = null;

        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            Orders o = ordersDAO.getOrderByID(Integer.parseInt(lblOID.getText()));
            double toPaidBalance = o.getToPaid() - amount;
            o.setToPaid(toPaidBalance);

            if (toPaidBalance == 0) {
                o.setPaymentStatus(true);
            }
            boolean update = ordersDAO.update(o);
            if (update) {
                boolean b = BalanceUpdating.addToBalance(amount);
                if (b) {
                    con.commit();
                    new Alert(Alert.AlertType.CONFIRMATION, "Success!").show();
                } else {
                    con.rollback();
                    new Alert(Alert.AlertType.ERROR, "Error While Updating").show();
                }
            } else {
                con.rollback();
                new Alert(Alert.AlertType.ERROR, "Error While Update Order").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            con.setAutoCommit(true);
        }
    }
}
