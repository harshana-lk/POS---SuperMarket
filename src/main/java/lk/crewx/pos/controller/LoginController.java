package lk.crewx.pos.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import lk.crewx.pos.util.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    public JFXTextField txtUser;
    public JFXPasswordField txtPass;

    public void initialize() {
        DBRefreshs.startRefreshDB();
    }

    public void logging(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        ResultSet execute = CrudUtil.execute("SELECT * FROM users WHERE userName=? && password=?", txtUser.getText(), txtPass.getText());
        if (execute.next()) {
            final String role = execute.getString("role");
            if (role.equals("admin")) {
                Env.user = "admin";
                Navigation.navigate(Routes.DASHBOARD, actionEvent);
            }
            if (role.equals("staff")) {
                Env.user = "staff";
                Navigation.navigate(Routes.DASHBOARD, actionEvent);
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "user name and password not match").show();
            txtPass.clear();
            txtUser.clear();
        }
    }
}
