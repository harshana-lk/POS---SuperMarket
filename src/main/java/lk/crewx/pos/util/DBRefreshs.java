package lk.crewx.pos.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

public class DBRefreshs {
    private static Thread t1;

    public static void startRefreshDB() {
        t1 = new Thread(DBRefreshs::refreshDB);
        t1.start();
    }

    private static void refreshDB() {
        LocalDate today = LocalDate.now();
        if (today.get(ChronoField.DAY_OF_MONTH) == 1) {

            // Get the first day of the month 3 months ago
            LocalDate firstDayOfMonth = today.minusMonths(3).with(TemporalAdjusters.firstDayOfMonth());

            // Get the last day of the month 3 months ago
            LocalDate lastDayOfMonth = today.minusMonths(3).with(TemporalAdjusters.lastDayOfMonth());

            try {
                // Calculate the total sales and subtotals for the month 3 months ago
                ResultSet salesRs = CrudUtil.execute("SELECT SUM(subtotal) FROM orders WHERE date >= ? AND date < ?",
                        java.sql.Date.valueOf(firstDayOfMonth),
                        java.sql.Date.valueOf(lastDayOfMonth));

                ResultSet supRes = CrudUtil.execute("SELECT SUM(amount) FROM supplier_transactions WHERE date >= ? AND date < ?",
                        java.sql.Date.valueOf(firstDayOfMonth),
                        java.sql.Date.valueOf(lastDayOfMonth));

                double supExpenses = 0;
                if (supRes.next()) {
                    supExpenses = supRes.getDouble(1);
                }
                double subtotal = 0;
                int totalOrders = 0;
                if (salesRs.next()) {
                    subtotal = salesRs.getDouble(1);
                }
                // Insert the calculated sales and subtotals into the new table
                CrudUtil.execute("INSERT INTO order_history (year,month, totalOrders, totalEarnings,totalCost,totalProfit) VALUES (?, ?, ?,?,?,?)",
                        firstDayOfMonth.getYear(),
                        firstDayOfMonth.getMonthValue(),
                        totalOrders,
                        subtotal,
                        supExpenses,
                        subtotal - supExpenses);

                // Delete the sales data that is older than 3 months

                CrudUtil.execute("DELETE FROM orders WHERE date < ?",
                        java.sql.Date.valueOf(lastDayOfMonth));

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                stopThread();
            }
        }
    }

    public static void stopThread() {
        try {
            t1.stop();
        } catch (NullPointerException e) {
            System.out.println("Sending error");
        }
    }
}
