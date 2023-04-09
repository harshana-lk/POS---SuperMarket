package lk.crewx.pos.dao.custom.impl;

import lk.crewx.pos.dao.custom.CategoryDAO;
import lk.crewx.pos.model.Category;
import lk.crewx.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDAOImpl implements CategoryDAO {


    @Override
    public boolean save(Category category) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO category(name) VALUES (?)",
                category.getName()
        );
    }

    @Override
    public ArrayList<Category> loadDataForTable() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Category category) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ArrayList<Category> filterDataForTable(String input) throws SQLException, ClassNotFoundException {
        return null;
    }

    public int getLastCategoryId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT id FROM category ORDER BY id DESC  LIMIT 1");

        while (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public ArrayList<Integer> setCategoryId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT Id FROM category");
        ArrayList<Integer> list = new ArrayList<>();

        while (rst.next()) {
            list.add(rst.getInt(1));
        }
        return list;
    }

    @Override
    public Category getCat(int id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM category WHERE id=?", id);

        if (rst.next()) {
            return new Category(
                    rst.getInt(1),
                    rst.getString(2)
            );
        }
        return null;
    }
}
