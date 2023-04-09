package lk.crewx.pos.dao.custom;

import lk.crewx.pos.dao.CrudDAO;
import lk.crewx.pos.model.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemDAO extends CrudDAO<Item,Integer,String> {

    public int getLastItemId() throws SQLException, ClassNotFoundException;

    public ArrayList<Integer> getItemIds() throws SQLException, ClassNotFoundException;

    public Item getItemDetails(Integer id) throws SQLException, ClassNotFoundException;


    ArrayList<Item> getCategoryItems(int selectedCategoryId) throws SQLException, ClassNotFoundException;
}
