package lk.crewx.pos.dao.custom.impl;

import lk.crewx.pos.dao.custom.OrdersDAO;
import lk.crewx.pos.model.Orders;
import lk.crewx.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersDAOImpl implements OrdersDAO {

    @Override
    public boolean save(Orders orders) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO orders(date,total,discount,subtotal,status,toPaid,name) VALUES(?,?,?,?,?,?,?)",
                orders.getDate(),
                orders.getTotal(),
                orders.getDiscount(),
                orders.getSubtotal(),
                orders.isPaymentStatus(),
                orders.getToPaid(),
                orders.getName());
    }

    @Override
    public ArrayList<Orders> loadDataForTable() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Orders orders) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE orders SET date=?, total=?, discount=?, subTotal=?, status=?, toPaid=?, name=? WHERE id=?",
                orders.getDate(),
                orders.getTotal(),
                orders.getDiscount(),
                orders.getSubtotal(),
                orders.isPaymentStatus(),
                orders.getToPaid(),
                orders.getName(),
                orders.getId()
        );
    }

    @Override
    public ArrayList<Orders> filterDataForTable(String input) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<Orders> getOrders(boolean input) throws SQLException, ClassNotFoundException {
        ArrayList<Orders> OrderList = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT * FROM orders WHERE status=?", input);
        while (rst.next()) {
            OrderList.add(new Orders(
                    rst.getInt(1),
                    rst.getDate(2).toLocalDate(),
                    rst.getDouble(3),
                    rst.getDouble(4),
                    rst.getDouble(5),
                    rst.getBoolean(6),
                    rst.getDouble(7),
                    rst.getString(8))

            );
        }
        return OrderList;
    }

    @Override
    public Orders getOrderByID(int id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM orders WHERE id=?", id);
        if (rst.next()) {
            return new Orders(
                    rst.getInt(1),
                    rst.getDate(2).toLocalDate(),
                    rst.getDouble(3),
                    rst.getDouble(4),
                    rst.getDouble(5),
                    rst.getBoolean(6),
                    rst.getDouble(7),
                    rst.getString(8)

            );
        }
        return null;
    }
}
