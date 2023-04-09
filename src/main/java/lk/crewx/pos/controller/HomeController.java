package lk.crewx.pos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.crewx.pos.dao.custom.ItemDAO;
import lk.crewx.pos.dao.custom.impl.ItemDAOImpl;
import lk.crewx.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HomeController {

    public AnchorPane dashboardContext;
    public Label lblOrders;
    public Label lblItems;
    public Label lblCategories;
    public Label lblSuppliers;
    public LineChart chartDailySales;
    public PieChart pieChart;
    public Label outOfStockName1;
    public Label outOfStockQTY1;
    public Label outOfStockName2;
    public Label outOfStockQTY2;
    public Label outOfStockName3;
    public Label outOfStockQTY3;
    public Label expName1;
    public Label expDate1;
    public Label expDate2;
    public Label expName3;
    public Label expDate3;
    public Label expName2;
    public Label trendingName1;
    public Label trendingQty1;
    public Label trendingName2;
    public Label trendingQty2;
    public Label trendingName3;
    public Label trendingQty3;

    ItemDAO itemDAO = new ItemDAOImpl();

    public void initialize() {
        setLabels();
        setChart();
        setPieChart();
//        setOutOfStockItems();
//        setExpireSoonItems();
//        setTrendingItems();
    }

    private void setTrendingItems() {
        try {
            ResultSet res = CrudUtil.execute("SELECT itemID, SUM(qty) as total_quantity FROM order_details GROUP BY itemID ORDER BY total_quantity DESC LIMIT 3");
            if (res.next()) {
                trendingName1.setText(itemDAO.getItemDetails(res.getInt(1)).getName());
                trendingQty1.setText(String.valueOf(res.getDouble(2)));
                if (res.next()) {
                    trendingName2.setText(itemDAO.getItemDetails(res.getInt(1)).getName());
                    trendingQty2.setText(String.valueOf(res.getDouble(2)));
                    if (res.next()) {
                        trendingName3.setText(itemDAO.getItemDetails(res.getInt(1)).getName());
                        trendingQty3.setText(String.valueOf(res.getDouble(2)));
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setExpireSoonItems() {
        try {
            ResultSet res = CrudUtil.execute("SELECT itemID, SUM(qty) as total_quantity FROM order_details GROUP BY itemID ORDER BY total_quantity ASC LIMIT 3");
            if (res.next()) {
                expName1.setText(itemDAO.getItemDetails(res.getInt(1)).getName());
                expDate1.setText(String.valueOf(res.getDouble(2)));
                if (res.next()) {
                    expName2.setText(itemDAO.getItemDetails(res.getInt(1)).getName());
                    expDate2.setText(String.valueOf(res.getDouble(2)));
                    if (res.next()) {
                        expName3.setText(itemDAO.getItemDetails(res.getInt(1)).getName());
                        expDate3.setText(String.valueOf(res.getDouble(2)));
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setOutOfStockItems() {
        try {
            ResultSet res = CrudUtil.execute("SELECT name, qty FROM items WHERE qty < 10 ORDER BY qty ASC LIMIT 3");
            if (res.next()) {
                System.out.println(res.getString(1));
                outOfStockName1.setText(res.getString(1));
                outOfStockQTY1.setText(String.valueOf(res.getInt(2)));
                if (res.next()) {
                    outOfStockName2.setText(res.getString(1));
                    outOfStockQTY2.setText(String.valueOf(res.getInt(2)));
                    if (res.next()) {
                        outOfStockName3.setText(res.getString(1));
                        outOfStockQTY3.setText(String.valueOf(res.getInt(2)));
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setPieChart() {

        try {
            ResultSet res = CrudUtil.execute("SELECT COUNT(*) FROM items WHERE qty <= 10");
            if (res.next()) {
                int exp = res.getInt(1);
                System.out.println(exp);
                ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM items");
                if (rs.next()) {
                    int total = rs.getInt(1);
                    System.out.println("total" + total);
                    int others = total - exp;
                    ObservableList<PieChart.Data> pieChartData =
                            FXCollections.observableArrayList(
                                    new PieChart.Data("Low", exp),
                                    new PieChart.Data("Good", others));
                    pieChart.setData(pieChartData);
                }

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Day of the Week");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total Sales");


        XYChart.Series series = new XYChart.Series();
        series.setName("Total Sales");

        try {
            ResultSet rs = CrudUtil.execute("SELECT DAYNAME(date) as day, SUM(subtotal) as sales FROM orders WHERE date BETWEEN DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND CURDATE() GROUP BY DAYNAME(date)");
            ArrayList<String> days = new ArrayList<>();
            ArrayList<Double> sales = new ArrayList<>();

            while (rs.next()) {
                days.add(rs.getString("day"));
                sales.add(rs.getDouble("sales"));
            }

            for (int i = 0; i < days.size(); i++) {
                series.getData().add(new XYChart.Data(days.get(i), sales.get(i)));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        chartDailySales.getData().add(series);


    }

    private void setLabels() {
        try {
            ResultSet res;
            res = CrudUtil.execute("SELECT COUNT(*) FROM orders WHERE MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE())");
            if (res.next()) {
                lblOrders.setText(String.valueOf(res.getInt(1)));
            }

            res = CrudUtil.execute("SELECT COUNT(*) FROM items");
            if (res.next()) {
                lblItems.setText(String.valueOf(res.getInt(1)));
            }

            res = CrudUtil.execute("SELECT COUNT(*) FROM category");
            if (res.next()) {
                lblCategories.setText(String.valueOf(res.getInt(1)));
            }

            res = CrudUtil.execute("SELECT COUNT(*) FROM suppliers");
            if (res.next()) {
                lblSuppliers.setText(String.valueOf(res.getInt(1)));
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
