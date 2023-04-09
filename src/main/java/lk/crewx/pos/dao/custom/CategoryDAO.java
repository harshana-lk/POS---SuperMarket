package lk.crewx.pos.dao.custom;

import lk.crewx.pos.dao.CrudDAO;
import lk.crewx.pos.model.Category;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CategoryDAO extends CrudDAO<Category,Integer,String> {

    public int getLastCategoryId() throws SQLException, ClassNotFoundException;

    public ArrayList<Integer> setCategoryId() throws SQLException, ClassNotFoundException;

    Category getCat(int id) throws SQLException, ClassNotFoundException;


    }
