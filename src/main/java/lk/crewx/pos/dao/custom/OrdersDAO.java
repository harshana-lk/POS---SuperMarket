package lk.crewx.pos.dao.custom;

import lk.crewx.pos.dao.CrudDAO;
import lk.crewx.pos.model.Orders;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrdersDAO extends CrudDAO<Orders,Integer,String> {
    ArrayList<Orders> getOrders(boolean input) throws SQLException, ClassNotFoundException;
    Orders getOrderByID(int id) throws SQLException, ClassNotFoundException;
}
