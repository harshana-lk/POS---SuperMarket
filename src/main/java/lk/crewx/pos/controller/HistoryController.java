package lk.crewx.pos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.crewx.pos.dao.custom.OrdersDAO;
import lk.crewx.pos.dao.custom.impl.OrdersDAOImpl;
import lk.crewx.pos.model.Orders;
import lk.crewx.pos.tm.OrderTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class HistoryController {
    public TableColumn colID;
    public TextField txtSearch;
    public TableView<OrderTM> tblHistory;
    public TableColumn colDate;
    public TableColumn colPrice;
    public TableColumn colAction;
    public Label lblUnpaid;
    public TableColumn toPaid;
    private OrdersDAO ordersDAO = new OrdersDAOImpl();

    public void initialize() throws SQLException, ClassNotFoundException {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
        toPaid.setCellValueFactory(new PropertyValueFactory<>("toPaid"));


        loadAllData();

    }

    public void unpaidOnAction(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException {
        ArrayList<Orders> OrderList = ordersDAO.getOrders(false);
        setData(OrderList);
    }

    private void loadAllData() throws SQLException, ClassNotFoundException {
        ArrayList<Orders> OrderList = ordersDAO.getOrders(true);
        setData(OrderList);
    }

    public void setData(ArrayList<Orders> orderList) {
        ObservableList<OrderTM> tmList = FXCollections.observableArrayList();
        for (Orders i : orderList) {
            Button btn = new Button("Update");
            btn.setDisable(i.isPaymentStatus());
            btn.setOnAction(ev -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoanPay.fxml"));
                    Parent parent = loader.load();
                    LoanPayController controller = loader.getController();
                    controller.setData(ordersDAO.getOrderByID(i.getId()));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(parent));
                    stage.show();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            });
            OrderTM tm = new OrderTM(
                    i.getId() + " - " + i.getName(),
                    i.getDate(),
                    i.getSubtotal(),
                    btn,
                    i.getToPaid());
            tmList.add(tm);
        }
        tblHistory.setItems(tmList);
    }
}