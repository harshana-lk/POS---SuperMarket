package lk.crewx.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.crewx.pos.dao.custom.ItemDAO;
import lk.crewx.pos.dao.custom.impl.ItemDAOImpl;
import lk.crewx.pos.model.Item;
import lk.crewx.pos.model.OrderDetails;
import lk.crewx.pos.model.Orders;
import lk.crewx.pos.tm.CartTm;
import lk.crewx.pos.tm.ItemTM;
import lk.crewx.pos.util.CrudUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderWindowController {
    static ObservableList<CartTm> obList = FXCollections.observableArrayList();
    private final ItemDAO itemDAO = new ItemDAOImpl();
    public JFXButton btnPlaceOrder;
    public Label lblTotal;
    public JFXButton btnSave;
    public JFXTextField txtBarrowName;
    public JFXTextField txtxPrice;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtQty;
    public JFXTextField txtDiscount;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colPrice;
    public TableColumn colQty;
    public TableColumn colDiscount;
    public TableColumn colTotal;
    public TableColumn colAction;
    public TableView<CartTm> tblPlaceOrder;
    public JFXTextField txtItemID;
    public JFXComboBox<String> cbxCategory;
    public TableColumn colItemID;
    public TableColumn colItemName;
    public TableView<ItemTM> tblItem;
    public TextField txtSearch;
    public Label lblDiscount;
    public JFXTextField textsName;
    public JFXButton btnRemove;
    Map<Integer, String> categories = getCategoriesFromDB();
    private int oID = 0;
    private double totalDiscount = 0;

    public void initialize() throws SQLException, ClassNotFoundException {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

        colItemID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));

        loadAllData();
        setCategories();
        tblItem.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        setDataForTextFields((ItemTM) newValue);
                    }

                });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    filterData(newValue);
                } else {
                    loadAllData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        tblPlaceOrder.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        setDataForTextFields((CartTm) newValue);
                    }

                });

    }

    private boolean checkQty(int code, double qty) {
        try {
            ResultSet set = CrudUtil.execute("SELECT qty FROM Items WHERE id=?", code);
            if (set.next()) {
                int tempQty = set.getInt(1);
                return tempQty >= qty;
            } else {
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void setCategories() {

        ObservableList<String> categoryNames = FXCollections.observableArrayList(categories.values());
        cbxCategory.setItems(categoryNames);

        cbxCategory.setOnAction(event -> {
            String selectedCategoryName = cbxCategory.getSelectionModel().getSelectedItem();
            int selectedCategoryId = -1;
            for (Map.Entry<Integer, String> entry : categories.entrySet()) {
                if (entry.getValue().equals(selectedCategoryName)) {
                    selectedCategoryId = entry.getKey();
                    break;
                }
            }
            try {
                ArrayList<Item> itemList = itemDAO.getCategoryItems(selectedCategoryId);
                setData(itemList);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

    }

    public void saveOnAction(ActionEvent actionEvent) {
        tblPlaceOrder.refresh();
        tblItem.refresh();
        setDataForTable();
        calTotal();
        try {
            loadAllData();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getLastItemId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT id FROM orders ORDER BY id DESC LIMIT 1");
        if (rst.next()) {
            return rst.getInt(1) + 1;
        }
        return -1;
    }

    public Map<Integer, String> getCategoriesFromDB() {
        Map<Integer, String> categories = new HashMap<>();

        try {
            ResultSet resultSet = CrudUtil.execute("SELECT id, name FROM category");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                categories.put(id, name);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException, IOException {
        try {
            oID = getLastItemId();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (obList.isEmpty()) return;
        ArrayList<OrderDetails> details = new ArrayList<>();

        for (CartTm c : obList
        ) {
            details.add(
                    new OrderDetails(
                            c.getUnitPrice(),
                            c.getQty(),
                            oID,
                            c.getId(),
                            c.getDiscount(),
                            c.getSubTotal()
                    )
            );
        }


        Orders order = new Orders(
                oID, //Add the last order ID here
                LocalDate.now(),
                Double.parseDouble(lblTotal.getText()),
                Double.parseDouble(lblDiscount.getText()),
                Double.parseDouble(lblTotal.getText()) - Double.parseDouble(lblDiscount.getText()),
                true,
                0,
                txtBarrowName.getText()
        );

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Summary.fxml"));
            Parent parent = loader.load();
            SummaryController controller = loader.getController();
            controller.setData(details, order);
            tblPlaceOrder.getItems().clear();
            obList.clear();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.show();
            lblTotal.setText("0.0");
            lblDiscount.setText("0.0");

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }


    private int alreadyExists(int id) {
        for (int i = 0; i < obList.size(); i++) {
            if (obList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private void calTotal() {
        double total = 0;

        for (CartTm tm : obList) {
            total += tm.getUnitPrice() * tm.getQty();
        }
        System.out.println(total);
        lblTotal.setText(String.valueOf(total));

    }

    private void calDiscount() {
        Double discount = 0.0;
        for (CartTm tm : obList) {
            discount += tm.getDiscount();
            lblDiscount.setText(String.valueOf(discount));
        }
    }


    public void setDataForTable() {
        if (!checkQty(Integer.parseInt(txtItemID.getText()), Double.parseDouble(txtQty.getText()))) {
            new Alert(Alert.AlertType.WARNING, "Invalid Qty").show();
            return;
        }
        int id = Integer.parseInt(txtItemID.getText());
        String name = textsName.getText();
        double unitPrice = Double.parseDouble(txtxPrice.getText());
        double qty = Double.parseDouble(txtQty.getText());
        double discount = Double.parseDouble(txtDiscount.getText());
        totalDiscount = discount * qty;
        double total = (qty * unitPrice);
        Button btn = new Button("Remove");
        btn.setStyle("-fx-background-color: #ff9f43; ");


        int row = alreadyExists(Integer.parseInt(txtItemID.getText()));

        if (row == -1) {
            CartTm tm = new CartTm(
                    id,
                    name,
                    unitPrice,
                    qty,
                    totalDiscount,
                    total - totalDiscount,
                    new Button("Remove")
            );
            tm.getBtn().setOnAction(event -> {
                obList.remove(tm);
                tblPlaceOrder.setItems(obList);
                calTotal();
                calDiscount();
            });
            obList.add(tm);
            tblPlaceOrder.setItems(obList);
            lblTotal.setText(String.valueOf(total));
            calDiscount();
        } else {
            double tempQty = (double) (obList.get(row).getQty() + qty);
            if (!checkQty(Integer.parseInt(txtItemID.getText()), tempQty)) {
                new Alert(Alert.AlertType.WARNING, "Invalid Qty").show();
                return;
            }
            double tempTotal = unitPrice * tempQty;
            double tempDis = discount * tempQty;
            obList.get(row).setQty(tempQty);
            obList.get(row).setSubTotal(tempTotal);
            obList.get(row).setDiscount(tempDis);
            tblPlaceOrder.refresh();
            lblTotal.setText(String.valueOf(tempTotal));
            lblDiscount.setText(String.valueOf(tempDis));
        }
        clear();


    }


    private void loadAllData() throws SQLException, ClassNotFoundException {
        ArrayList<Item> itemList = itemDAO.loadDataForTable();
        setData(itemList);
    }

    private void filterData(String input) throws SQLException, ClassNotFoundException {
        ArrayList<Item> itemList = itemDAO.filterDataForTable(input);
        setData(itemList);
    }

    public void setData(ArrayList<Item> itemList) throws SQLException, ClassNotFoundException {
        ObservableList<ItemTM> tmList = FXCollections.observableArrayList();

        for (Item c : itemList) {
            ItemTM tm = new ItemTM(
                    c.getId(),
                    c.getName(),
                    c.getSellingPrice(),
                    c.getSupplierID(),
                    c.getCategory(),
                    c.getQty(),
                    c.getDiscount()

            );
            tmList.add(tm);
            tblItem.setItems(tmList);


        }
    }

    public void clear() {
        textsName.clear();
        txtQtyOnHand.clear();
        txtQty.clear();
        txtDiscount.clear();
        txtxPrice.clear();
    }

    public void setDataForTextFields(ItemTM tm) {
        txtxPrice.setText(String.valueOf(tm.getSellingPrice()));
        txtItemID.setText(String.valueOf(tm.getId()));
        textsName.setText(tm.getName());
        txtQtyOnHand.setText(String.valueOf(tm.getQty()));
        txtDiscount.setText(String.valueOf(tm.getDiscount()));
    }

    public void setDataForTextFields(CartTm tm) {
        txtItemID.setText(String.valueOf(tm.getId()));
        textsName.setText(tm.getName());
        txtxPrice.setText(String.valueOf(tm.getUnitPrice()));
        txtQty.setText(String.valueOf(tm.getQty()));
        txtDiscount.setText(String.valueOf(tm.getDiscount()));

    }

}

