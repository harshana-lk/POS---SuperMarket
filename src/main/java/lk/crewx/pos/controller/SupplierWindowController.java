package lk.crewx.pos.controller;

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
import lk.crewx.pos.dao.custom.SupplierDAO;
import lk.crewx.pos.dao.custom.impl.SuppliersDAOImpl;
import lk.crewx.pos.model.Suppliers;
import lk.crewx.pos.tm.SupplierTM;
import lk.crewx.pos.util.Validator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class SupplierWindowController {

    public TableView<SupplierTM> tblSupplier;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colPhone;
    public Button btnDelete;
    public Button btnAdd;
    public JFXTextField txtSupID;
    public JFXTextField txtPhone;
    public JFXTextField txtAddress;
    public JFXTextField txtName;
    public TextField txtSearch;
    public TableColumn colAction;
    public Button btnUpdate;
    private SupplierDAO supplierDAO = new SuppliersDAOImpl();

    public void initialize() throws SQLException, ClassNotFoundException {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        loadAllData();
        setSupplierId();

        tblSupplier.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        setDataForTextFields((SupplierTM) newValue);
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
        int id = Integer.parseInt(txtSupID.getText());
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure whether do you want to delete this supplier?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get() == ButtonType.YES) {
                boolean isDelete = supplierDAO.delete(id);
                if (isDelete) {
                    new Alert(Alert.AlertType.CONFIRMATION, "supplier Deleted!").show();
                    clear();
                    loadAllData();
                    setSupplierId();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addOnAction(ActionEvent actionEvent) {
        int id = Integer.parseInt(txtSupID.getText());

        Suppliers suppliers = new Suppliers(
                id,
                txtName.getText(),
                txtAddress.getText(),
                txtPhone.getText()
        );
        if (txtName.getText().equals("") || txtAddress.getText().equals("") || txtPhone.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "plase enter correct details!").show();

        } else if (Validator.isNameMatch(txtName.getText())) {
            if (Validator.isAddressMatch(txtAddress.getText())) {
                if (Validator.isPhoneNumberMatch(txtPhone.getText())) {
                    try {
                        boolean isSaved = supplierDAO.save(suppliers);
                        if (isSaved) {
                            clear();
                            setSupplierId();
                            loadAllData();
                            new Alert(Alert.AlertType.CONFIRMATION, "Supplier Added!").show();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    new Alert(Alert.AlertType.ERROR, "please enter correct phone number!").show();
                    txtPhone.requestFocus();

                }

            } else {
                new Alert(Alert.AlertType.ERROR, "please enter correct address!").show();
                txtAddress.requestFocus();

            }

        } else {
            new Alert(Alert.AlertType.ERROR, "please enter correct name!").show();
            txtName.requestFocus();

        }


    }

    public void setSupplierId() {
        try {
            int lastSuId = supplierDAO.getLastSupplierId();
            if (lastSuId == -1) {
                txtSupID.setText("1");
            } else {
                txtSupID.setText(String.valueOf(lastSuId + 1));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void loadAllData() throws SQLException, ClassNotFoundException {
        ArrayList<Suppliers> supplierList = supplierDAO.loadDataForTable();
        setData(supplierList);
    }

    private void filterData(String input) throws SQLException, ClassNotFoundException {
        ArrayList<Suppliers> supplierList = new SuppliersDAOImpl().filterDataForTable(input);
        setData(supplierList);
    }

    public void setData(ArrayList<Suppliers> supplierList) throws SQLException, ClassNotFoundException {
        ObservableList<SupplierTM> tmList = FXCollections.observableArrayList();

        for (Suppliers s : supplierList) {
            Button btn = new Button("Pay");
            btn.setOnAction(ev -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PaySupplier.fxml"));
                    Parent parent = loader.load();
                    PaySupplierController controller = loader.getController();
                    controller.setData(new Suppliers(
                            s.getId(),
                            s.getName(),
                            s.getAddress(),
                            s.getTelephone()
                    ));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(parent));
                    stage.show();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            SupplierTM tm = new SupplierTM(
                    s.getId(),
                    s.getName(),
                    s.getAddress(),
                    s.getTelephone(),
                    btn
            );
            tmList.add(tm);
        }

        tblSupplier.setItems(tmList);
    }

    public void clear() {
        txtName.clear();
        txtAddress.clear();
        txtPhone.clear();

        btnAdd.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    public void setDataForTextFields(SupplierTM tm) {
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
        txtSupID.setText(String.valueOf(tm.getId()));
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtPhone.setText(tm.getTelephone());
    }

    public void updateOnAction(ActionEvent actionEvent) {


        if (txtName.getText().equals("") || txtAddress.getText().equals("") || txtPhone.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "plase enter correct details!").show();

        } else if (Validator.isNameMatch(txtName.getText())) {
            if (Validator.isAddressMatch(txtAddress.getText())) {
                try {
                    int id = Integer.parseInt(txtSupID.getText());

                    Suppliers suppliers = new Suppliers(
                            id,
                            txtName.getText(),
                            txtAddress.getText(),
                            txtPhone.getText().length() > 0 ? String.valueOf(txtPhone.getText().length()) : "0000000"
                    );

                    boolean isUpdated = supplierDAO.update(suppliers);

                    if (isUpdated) {
                        clear();
                        loadAllData();
                        setSupplierId();
                        new Alert(Alert.AlertType.CONFIRMATION, "Supplier Updated!").show();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "please enter correct address!").show();
                txtAddress.requestFocus();

            }

        } else {
            new Alert(Alert.AlertType.ERROR, "please enter correct name!").show();
            txtName.requestFocus();

        }


    }
}
