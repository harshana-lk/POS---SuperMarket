package lk.crewx.pos.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.scene.control.Label;
import lk.crewx.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonthlyReportController {
    public Label lblTotalOrders;
    public Label lblTotalEarnings;
    public Label lblTotalExpenses;
    public Label lblOnHand;
    public Label lblDate;
    public JFXComboBox cmbMonths;


    public void initialize() {
        // Fill the combo box with the months and years that have orders
        cmbMonths.getItems().addAll(getMonthsAndYearsWithOrders());
        cmbMonths.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        updateReport();
                    }
                });
    }

    private void updateReport() {
        String selected = (String) cmbMonths.getValue();
        String[] date = selected.split("-");
        int month = Integer.parseInt(date[0]);
        int year = Integer.parseInt(date[1]);
        lblDate.setText(month + "-" + year);

        // Check if the selected month is within the last 3 months
        LocalDate today = LocalDate.now();
        LocalDate selectedDate = LocalDate.of(year, month, 1);
        if (!selectedDate.isBefore(LocalDate.now().minusMonths(3))) {
            System.out.println("Getting from orders table");

            // Get the total number of orders for the selected month from the orders table
            int totalOrders = getMonthlyOrders(month, year);
            lblTotalOrders.setText(Integer.toString(totalOrders));

            // Get the total earnings for the selected month from the orders table
            int totalEarnings = getMonthlyEarnings(month, year);
            lblTotalEarnings.setText(Integer.toString(totalEarnings));

            // Get the total expenses for the selected month from the supplier transactions table
            int totalExpenses = getMonthlyExpenses(month, year);
            lblTotalExpenses.setText(Integer.toString(totalExpenses));

            // Calculate on hand value by subtracting expenses from earnings
            int onHand = totalEarnings - totalExpenses;
            lblOnHand.setText(Integer.toString(onHand));
        } else {
            System.out.println("Getting from orders_history table");

            // Get the data for the selected month and year from the order_history table
            int totalOrders = getMonthlyOrdersFromHistory(month, year);
            lblTotalOrders.setText(Integer.toString(totalOrders));

            int totalEarnings = getMonthlyEarningsFromHistory(month, year);
            lblTotalEarnings.setText(Integer.toString(totalEarnings));

            int totalExpenses = getMonthlyExpensesFromHistory(month, year);
            lblTotalExpenses.setText(Integer.toString(totalExpenses));

            int onHand = totalEarnings - totalExpenses;
            lblOnHand.setText(Integer.toString(onHand));
        }
    }

    private List<String> getMonthsAndYearsWithOrders() {
        List<String> months = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.execute("SELECT DISTINCT MONTH(date), YEAR(date) FROM orders");
            while (rs.next()) {
                int monthValue = rs.getInt(1);
                int yearValue = rs.getInt(2);
                String monthAndYear = monthValue + "-" + yearValue;
                months.add(monthAndYear);
            }
            ResultSet res = CrudUtil.execute("SELECT month, year FROM order_history");
            while (res.next()) {
                int monthValue = res.getInt(1);
                int yearValue = res.getInt(2);
                String monthAndYear = monthValue + "-" + yearValue;
                months.add(monthAndYear);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return months;
    }

    private int getMonthlyOrders(int month, int year) {
        int totalOrders = 0;
        try {
            ResultSet rs = CrudUtil.execute("SELECT COUNT(id) FROM orders WHERE MONTH(date) = ? AND YEAR(date) = ?", month, year);
            if (rs.next()) {
                totalOrders = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return totalOrders;
    }

    private int getMonthlyEarnings(int month, int year) {
        int totalEarnings = 0;
        try {
            ResultSet rs = CrudUtil.execute("SELECT SUM(subtotal) FROM orders WHERE MONTH(date) = ? AND YEAR(date) = ?", month, year);
            if (rs.next()) {
                totalEarnings = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return totalEarnings;
    }

    private int getMonthlyExpenses(int month, int year) {
        int totalExpenses = 0;
        try {
            ResultSet rs = CrudUtil.execute("SELECT SUM(amount) FROM supplier_transactions WHERE MONTH(date) = ? AND YEAR(date) = ?", month, year);
            if (rs.next()) {
                totalExpenses = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return totalExpenses;
    }
    private int getMonthlyOrdersFromHistory(int month, int year) {
        try {
            ResultSet rst = CrudUtil.execute("SELECT totalOrders FROM order_history WHERE month = ? AND year = ?", month, year);
            if (rst.next()) {
                return rst.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private int getMonthlyEarningsFromHistory(int month, int year) {
        try {
            ResultSet rst = CrudUtil.execute("SELECT totalEarnings FROM order_history WHERE month = ? AND year = ?", month, year);
            if (rst.next()) {
                return rst.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private int getMonthlyExpensesFromHistory(int month, int year) {
        try {
            ResultSet rst = CrudUtil.execute("SELECT totalCost FROM order_history WHERE month = ? AND year = ?", month, year);
            if (rst.next()) {
                return rst.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}