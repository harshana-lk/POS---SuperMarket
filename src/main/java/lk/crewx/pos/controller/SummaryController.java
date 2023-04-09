package lk.crewx.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lk.crewx.pos.dao.custom.OrdersDAO;
import lk.crewx.pos.dao.custom.impl.OrdersDAOImpl;
import lk.crewx.pos.db.BalanceUpdating;
import lk.crewx.pos.db.DBConnection;
import lk.crewx.pos.model.OrderDetails;
import lk.crewx.pos.model.Orders;
import lk.crewx.pos.util.CrudUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SummaryController {
    private final OrdersDAO ordersDAO = new OrdersDAOImpl();
    public Label lblPurchaseID;
    public Label lblDate;
    public JFXTextField txtCash;
    public JFXButton btnPay;
    public Label lblBalance;
    public JFXButton btnClose;
    public Label lblSubTotal;
    private Orders order;
    private ArrayList<OrderDetails> details;
    private boolean isLoan;

    public SummaryController() throws IOException {
    }


    public void initialize() {
        txtCash.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double amount = Double.parseDouble(newValue);
                if (!newValue.isEmpty()) {
                    lblBalance.setText(String.valueOf((Double.parseDouble(lblSubTotal.getText()) - amount) * -1));
                } else {
                    lblBalance.setText(String.valueOf(0));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void setData(ArrayList<OrderDetails> details, Orders order) throws SQLException {
        this.order = order;
        this.details = details;

        lblPurchaseID.setText(String.valueOf(order.getId()));
        lblDate.setText(String.valueOf(order.getDate()));
        lblSubTotal.setText(String.valueOf(order.getSubtotal()));

    }

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OrderWindow.fxml"));
    Parent parent = loader.load();
    OrderWindowController controller = loader.getController();

    public void payOnAction(ActionEvent actionEvent) throws IOException {

        isLoan = Double.parseDouble(txtCash.getText()) < Double.parseDouble(lblSubTotal.getText());
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            if (isLoan) {
                order.setPaymentStatus(false);
                order.setToPaid(-1 * Double.parseDouble(lblBalance.getText()));
                System.out.println(order);
                BalanceUpdating.addToBalance(order.getToPaid());
            } else {
                BalanceUpdating.addToBalance(order.getSubtotal());
            }
            boolean isOrderSaved = ordersDAO.save(order);
            if (isOrderSaved) {
                boolean isAllUpdated = manageQty(details);
                if (isAllUpdated) {
                    con.commit();
                    new Alert(Alert.AlertType.CONFIRMATION, "Order Placed!").show();
                    Stage stage =(Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.close();
                    controller.tblItem.refresh();
                } else {
                    con.rollback();
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }

            } else {
                con.rollback();
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println(String.valueOf(order.getTotal()- order.getSubtotal()));
        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OrderWindow.fxml"));
//            Parent parent = loader.load();
//            OrderWindowController controller = loader.getController();

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("parameter1", String.valueOf(order.getId()));
            hm.put("dateTime", order.getDate().toString());
            hm.put("grossAmount", String.valueOf(order.getTotal()));
            hm.put("discount",String.valueOf(order.getTotal()- order.getSubtotal())) ;
//            System.out.println(order.getTotal()- order.getSubtotal());
            hm.put("netAmount", String.valueOf(order.getSubtotal()));
            hm.put("payment", String.valueOf(txtCash.getText()));
            double balance = Double.parseDouble(txtCash.getText()) - order.getSubtotal();
            String balanceLbl = balance < 0 ? "Loan : " + -1 * balance : String.valueOf(balance);
            hm.put("balance", balanceLbl);


            JasperDesign jDesign = JRXmlLoader.load("C:/crewx/invoice.jrxml");
            JasperReport compileReport = null;
            try {
                compileReport = JasperCompileManager.compileReport(jDesign);
            } catch (JRException e) {
                e.printStackTrace();
            }
            try {
                JasperPrint jasperPrint = JasperFillManager
                        .fillReport(compileReport, hm, DBConnection.getInstance().getConnection());
                JasperPrintManager.printReport(jasperPrint, true);
            } catch (JRException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("--printed--");
        } catch (Exception var16) {
            var16.printStackTrace();
        }


    }

    private boolean manageQty(ArrayList<OrderDetails> details) throws SQLException, ClassNotFoundException {
        for (OrderDetails od : details
        ) {
            boolean b = CrudUtil.execute("INSERT INTO order_details(oID,itemID,qty,price,discount,subtotal) VALUES(?,?,?,?,?,?)",
                    od.getoID(),
                    od.getItemID(),
                    od.getQty(),
                    od.getPrice(),
                    od.getDiscount(),
                    od.getSubTotal()
            );

            if (b) {
                new Alert(Alert.AlertType.INFORMATION, "Added to order_details table");
                boolean isQtyUpdated = update(od);
                if (!isQtyUpdated) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean update(OrderDetails od) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE items SET qty=(qty-?) WHERE id=?",
                od.getQty(),
                od.getItemID());
    }
}