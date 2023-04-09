package lk.crewx.pos.dao.custom.impl;

import lk.crewx.pos.dao.custom.OrderDetailsDAO;
import lk.crewx.pos.model.OrderDetails;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {

    @Override
    public boolean save(OrderDetails orderDetails) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ArrayList<OrderDetails> loadDataForTable() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(OrderDetails orderDetails) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ArrayList<OrderDetails> filterDataForTable(String input) throws SQLException, ClassNotFoundException {
        return null;
    }
}
