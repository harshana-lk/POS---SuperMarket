package lk.crewx.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lk.crewx.pos.dao.custom.CategoryDAO;
import lk.crewx.pos.dao.custom.impl.CategoryDAOImpl;
import lk.crewx.pos.model.Category;
import lk.crewx.pos.util.Validator;

import java.sql.SQLException;

public class CategoryWindowController {
    public Button btnAdd;
    public JFXButton btnClose;
    public JFXTextField txtCategoryID;
    public JFXTextField txtCategoryName;
    private CategoryDAO categoryDAO = new CategoryDAOImpl();

    public void initialize() {
        setSupplierId();
    }


    public void addOnAction(ActionEvent actionEvent) {
        int id = Integer.parseInt(txtCategoryID.getText());

        Category category = new Category(
                id,
                txtCategoryName.getText()
        );
        if (txtCategoryName.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Enter Correct details first").show();

        } else if (Validator.isNameMatch(txtCategoryName.getText())) {
            try {
                boolean isSaved = categoryDAO.save(category);
                if (isSaved) {
                    clear();
                    setSupplierId();
                    new Alert(Alert.AlertType.CONFIRMATION, "Category Added!").show();
                    Stage stage =(Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.hide();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Enter Correct name!").show();

        }


    }

    public void CloseOnAction(ActionEvent actionEvent) {
    }

    public void setSupplierId() {
        try {
            int lastCategoryId = categoryDAO.getLastCategoryId();
            if (lastCategoryId == -1) {
                txtCategoryID.setText("1");
            } else {
                txtCategoryID.setText(String.valueOf(lastCategoryId + 1));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        txtCategoryName.clear();
    }
}
