package lk.crewx.pos.controller;

import javafx.scene.control.Label;
import lk.crewx.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DailyReportController {
    public Label lblTotalOrders;
    public Label lblTotalEarnings;
    public Label lblDate;
    public Label lblExpenses;
    public Label lblOnHand;

    public void initialize() {
        lblDate.setText(LocalDate.now().toString());

        int totalOrders = getTodaysOrders();
        lblTotalOrders.setText(Integer.toString(totalOrders));

        double totalEarnings = getTodaysEarnings();
        lblTotalEarnings.setText(String.valueOf((totalEarnings)));

        double totalExpenses = getTodaysExpenses();
        lblExpenses.setText(String.valueOf(totalExpenses));

        double onHand = getOnHand();
        lblOnHand.setText(String.valueOf(onHand));
    }

    private double getOnHand() {
        try {
            ResultSet res = CrudUtil.execute("SELECT balance FROM money WHERE id=0");
            if (res.next()) {
                return res.getDouble(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getTodaysOrders() {
        LocalDate today = LocalDate.now();
        int totalOrders = 0;
        try {
            ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM orders WHERE date = ?", today);
            if (rs.next()) {
                totalOrders = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return totalOrders;
    }

    private double getTodaysEarnings() {
        LocalDate today = LocalDate.now();
        double totalEarnings = 0;
        try {
            ResultSet rs = CrudUtil.execute("SELECT SUM(subtotal) FROM orders WHERE date = ?", today);

            if (rs.next()) {
                totalEarnings = rs.getDouble(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return totalEarnings;

    }

    private double getTodaysExpenses() {
        LocalDate today = LocalDate.now();
        double totalExpenses = 0;
        try {
            ResultSet rs = CrudUtil.execute("SELECT SUM(amount) FROM supplier_transactions WHERE date = ?", today);
            if (rs.next()) {
                totalExpenses = rs.getDouble(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return totalExpenses;
    }


}
