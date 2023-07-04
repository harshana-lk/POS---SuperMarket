package lk.crewx.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.crewx.pos.dao.custom.CategoryDAO;
import lk.crewx.pos.dao.custom.ItemDAO;
import lk.crewx.pos.dao.custom.SupplierDAO;
import lk.crewx.pos.dao.custom.impl.CategoryDAOImpl;
import lk.crewx.pos.dao.custom.impl.ItemDAOImpl;
import lk.crewx.pos.dao.custom.impl.SuppliersDAOImpl;
import lk.crewx.pos.db.DBConnection;
import lk.crewx.pos.model.Item;
import lk.crewx.pos.tm.ItemTM;
import lk.crewx.pos.util.CrudUtil;
import lk.crewx.pos.util.Validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemWindowController {

    private final SupplierDAO supplierDAO = new SuppliersDAOImpl();
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();
    private final ItemDAO itemDAO = new ItemDAOImpl();
    public JFXTextField txtName;
    public JFXTextField txtItemID;
    public Button btnAdd;
    public Button btnDelete;
    public JFXTextField txtQty;
    public JFXComboBox<String> cbxCategory;
    public JFXComboBox cbxSuppID;
    public Button btnUpdate;
    public TableView tblSupplier;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colCategory;
    public TableColumn colSuppID;
    public TableColumn colBuyingPrice;
    public TableColumn colSellingPrice;
    public TableColumn colExpDate;
    public TableColumn colQty;
    public TextField txtSearch;
    public JFXButton btnCategory;
    public JFXTextField txtDiscount;
    public JFXTextField txtSellingPrice;
    public TableColumn colDiscount;
    public AnchorPane pane;

    Map<Integer, String> categories = getCategoriesFromDB();
    private int categoryID = 0;

    public void initialize() throws SQLException, ClassNotFoundException {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colSuppID.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        loadAllData();
        setItemId();
        setSupplierId();
        setCategories();

        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);

        tblSupplier.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        try {
                            setDataForTextFields((ItemTM) newValue);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!newValue.isEmpty() && newValue != null) {
                    filterData(newValue);
                } else {
                    loadAllData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        int id = Integer.parseInt(txtItemID.getText());

        try {
            boolean isDeleted = itemDAO.delete(id);
            if (isDeleted) {
                clear();
                setItemId();
                loadAllData();
                new Alert(Alert.AlertType.CONFIRMATION, "Item Deleted!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setCategories() {

        ObservableList<String> categoryNames = FXCollections.observableArrayList(categories.values());
        cbxCategory.setItems(categoryNames);

        cbxCategory.setOnAction(event -> {
            String selectedCategoryName = cbxCategory.getSelectionModel().getSelectedItem();
            for (Map.Entry<Integer, String> entry : categories.entrySet()) {
                if (entry.getValue().equals(selectedCategoryName)) {
                    categoryID = entry.getKey();
                    break;
                }
            }
        });
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

    public void updateOnAction(ActionEvent actionEvent) {
        if (txtName.getText().equals("") || txtQty.getText().equals("") || txtSellingPrice.getText().equals("")
                || txtDiscount.getText().equals("") || cbxSuppID.getValue().equals("") || cbxCategory.getValue().equals("")) {
            new Alert(Alert.AlertType.ERROR, "please enter correct details!").show();
        } else if (Validator.isNameMatch(txtName.getText())) {
            if (Validator.isPriceMatch(txtDiscount.getText())) {
                if (Validator.isPriceMatch(txtSellingPrice.getText())) {
                    if (!(Double.parseDouble(txtDiscount.getText()) > Double.parseDouble(txtSellingPrice.getText()))) {
                        try {
                            int id = Integer.parseInt(txtItemID.getText());
                            double sellingPrice = Double.parseDouble(txtSellingPrice.getText());
                            double qty = Double.parseDouble((txtQty.getText()));
                            int supId = Integer.parseInt(cbxSuppID.getValue().toString());
                            int catId = categoryID;

                            double discount = Double.parseDouble(txtDiscount.getText());

                            Item item = new Item(
                                    id,
                                    txtName.getText(),
                                    sellingPrice,
                                    supId,
                                    catId,
                                    qty,
                                    discount
                            );
                            boolean isUpdated = itemDAO.update(item);
                            if (isUpdated) {
                                clear();
                                setItemId();
                                loadAllData();
                                new Alert(Alert.AlertType.CONFIRMATION, "Item Updated!").show();

                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Price less than discount").show();
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "enter correct price!").show();
                    txtSellingPrice.requestFocus();
                }

            } else {
                new Alert(Alert.AlertType.ERROR, "enter correct discount!").show();
                txtDiscount.requestFocus();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "enter correct Name!").show();
            txtName.requestFocus();
        }
    }

    public void addOnAction(ActionEvent actionEvent) {
        if (txtName.getText().equals("") || txtQty.getText().equals("") || txtSellingPrice.getText().equals("")
                || txtDiscount.getText().equals("") || cbxSuppID.getValue().equals("") || cbxCategory.getValue().equals("")) {
            new Alert(Alert.AlertType.ERROR, "please enter correct details!").show();
        } else if (Validator.isNameMatch(txtName.getText())) {
            if (Validator.isPriceMatch(txtDiscount.getText())) {
                if (Validator.isPriceMatch(txtSellingPrice.getText())) {
                    try {
                        int id = Integer.parseInt(txtItemID.getText());
                        double sellingPrice = Double.parseDouble(txtSellingPrice.getText());
                        int qty = Integer.parseInt(txtQty.getText());
                        int supId = Integer.parseInt(cbxSuppID.getValue().toString());
                        int catId = categoryID;
                        double discount = Double.parseDouble(txtDiscount.getText());

                        Item item = new Item(
                                id,
                                txtName.getText(),
                                sellingPrice,
                                supId,
                                catId,
                                qty,
                                discount
                        );

                        boolean isSaved = itemDAO.save(item);

                        if (isSaved) {
                            clear();
                            loadAllData();
                            setItemId();
                            new Alert(Alert.AlertType.CONFIRMATION, "Item Added!").show();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "enter correct price!").show();
                    txtSellingPrice.requestFocus();

                }

            } else {
                new Alert(Alert.AlertType.ERROR, "enter correct discount!").show();
                txtDiscount.requestFocus();

            }


        } else {
            new Alert(Alert.AlertType.ERROR, "enter correct name!").show();
            txtName.requestFocus();

        }


    }

    public void newCategoryOnAction(ActionEvent actionEvent) throws IOException {
//        Navigation.navigate(Routes.CATEGORY, pane);
        Scene scene = null;
        scene = new Scene(FXMLLoader.load(getClass().getResource("/view/CategoryWindow.fxml")));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void setSupplierId() throws SQLException, ClassNotFoundException {
        ObservableList<Object> data = FXCollections.observableArrayList(supplierDAO.getSupplierId());
        cbxSuppID.setItems(data);
    }

    public void setItemId() {
        try {
            int lastItemId = itemDAO.getLastItemId();
            if (lastItemId == -1) {
                txtItemID.setText("1");
            } else {
                txtItemID.setText(String.valueOf(lastItemId + 1));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllData() throws SQLException, ClassNotFoundException {
        ArrayList<Item> itemList = itemDAO.loadDataForTable();
        setData(itemList);
    }

    private void filterData(String input) throws SQLException, ClassNotFoundException {
        ArrayList<Item> supplierList = new ItemDAOImpl().filterDataForTable(input);
        setData(supplierList);
    }

    public void setData(ArrayList<Item> itemList) {
        ObservableList<ItemTM> tmList = FXCollections.observableArrayList();

        for (Item i : itemList) {
            ItemTM tm = new ItemTM(
                    i.getId(),
                    i.getName(),
                    i.getSellingPrice(),
                    i.getSupplierID(),
                    i.getCategory(),
                    i.getQty(),
                    i.getDiscount()

            );
            tmList.add(tm);

        }

        tblSupplier.setItems(tmList);
    }

    public void setDataForTextFields(ItemTM tm) throws SQLException, ClassNotFoundException {
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
        txtItemID.setText(String.valueOf(tm.getId()));
        txtName.setText(tm.getName());
        txtSellingPrice.setText(String.valueOf(tm.getSellingPrice()));
        txtQty.setText(String.valueOf(tm.getQty()));
        txtDiscount.setText(String.valueOf(tm.getDiscount()));
        cbxSuppID.setValue(tm.getSupplierID());
        cbxCategory.setValue(String.valueOf(categoryDAO.getCat(tm.getCategory()).getName()));

    }

    public void clear() {
        txtName.clear();
        txtSellingPrice.clear();
        txtQty.clear();
        txtDiscount.clear();
        cbxSuppID.getSelectionModel().clearSelection();
        cbxCategory.getSelectionModel().clearSelection();
        btnAdd.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    public void importAction(ActionEvent actionEvent) throws SQLException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String csvFilePath = selectedFile.getAbsolutePath();
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText;
            lineReader.readLine();

            Connection connection = null;
            try {
                boolean b = true;
                connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);
                while ((lineText = lineReader.readLine()) != null) {
                    String[] data = lineText.split(",");
                    String name = data[0];
                    double buyingPrice = Double.parseDouble(data[1]);
                    int supplier = Integer.parseInt(data[2]);
                    int category = Integer.parseInt(data[3]);
                    double qty = Double.parseDouble(data[4]);
                    double discount = Double.parseDouble(data[5]);

                    b = CrudUtil.execute("INSERT INTO items(name,sellingPrice,supplierID,category,qty,discount) VALUES(?,?,?,?,?,?)",
                            name,
                            buyingPrice,
                            supplier,
                            category,
                            qty,
                            discount
                    );
                }

                if (b) {
                    connection.commit();
                    connection.setAutoCommit(true);
                } else {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
            lineReader.close();
        }
    }

}