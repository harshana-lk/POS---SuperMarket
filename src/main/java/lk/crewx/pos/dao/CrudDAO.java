package lk.crewx.pos.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T,ID,S>{
    boolean save(T t) throws SQLException, ClassNotFoundException;

    ArrayList<T> loadDataForTable() throws SQLException, ClassNotFoundException;

    boolean delete(ID id) throws SQLException, ClassNotFoundException;

    boolean update(T t) throws SQLException, ClassNotFoundException;

    ArrayList<T> filterDataForTable(S input) throws SQLException, ClassNotFoundException;
}
