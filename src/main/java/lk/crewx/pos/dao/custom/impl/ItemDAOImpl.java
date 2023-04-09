package lk.crewx.pos.dao.custom.impl;



import lk.crewx.pos.dao.custom.ItemDAO;
import lk.crewx.pos.model.Item;
import lk.crewx.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public boolean save(Item item) throws SQLException, ClassNotFoundException {

        return CrudUtil.execute("INSERT INTO items(name,sellingPrice,supplierID, category,qty,discount) VALUES (?,?,?,?,?,?)",
                item.getName(),
                item.getSellingPrice(),
                item.getSupplierID(),
                item.getCategory(),
                item.getQty(),
                item.getDiscount()
        );
    }

    @Override
    public ArrayList<Item> loadDataForTable() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM items");
        ArrayList<Item> list = new ArrayList<>();

        while (rst.next()) {
            list.add(new Item(
                    rst.getInt(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getDouble(7)
            ));
        }
        return list;
    }

    @Override
    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM items WHERE id = ?", id);
    }

    @Override
    public boolean update(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE items SET  name=?, sellingPrice=?,  supplierID=?, category=?, qty=?, discount=? WHERE id=?",
                item.getName(),
                item.getSellingPrice(),
                item.getSupplierID(),
                item.getCategory(),
                item.getQty(),
                item.getDiscount(), item.getId()


        );
    }

    @Override
    public ArrayList<Item> filterDataForTable(String input) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM items WHERE name like '%" + input + "%' || category like '%" + input + "%' || id like '%" + input + "%'");
        ArrayList<Item> itemList = new ArrayList<>();

        while (rst.next()) {
            itemList.add(new Item(
                    rst.getInt(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getDouble(7)
            ));
        }
        return itemList;
    }

    public ArrayList<Item> getCategoryItems(int input) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM items WHERE category =?", input);
        ArrayList<Item> itemList = new ArrayList<>();

        while (rst.next()) {
            itemList.add(new Item(
                            rst.getInt(1),
                            rst.getString(2),
                            rst.getDouble(3),
                            rst.getInt(4),
                            rst.getInt(5),
                            rst.getDouble(6),
                            rst.getDouble(7)
                    )

            );
        }
        return itemList;
    }


    public int getLastItemId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT id FROM items ORDER BY id DESC  LIMIT 1");

        if (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public ArrayList<Integer> getItemIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT Id FROM items");
        ArrayList<Integer> list = new ArrayList<>();

        while (rst.next()) {
            list.add(rst.getInt(1));
        }
        return list;
    }


    public Item getItemDetails(Integer id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM items WHERE id = ?", id);

        if (rst.next()) {
            return new Item(
                    rst.getInt(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getDouble(7)
            );
        }
        return null;
    }

}