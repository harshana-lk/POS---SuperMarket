package lk.crewx.pos.dao.custom.impl;

import lk.crewx.pos.dao.custom.UsersDAO;
import lk.crewx.pos.model.Users;

import java.sql.SQLException;
import java.util.ArrayList;

public class UsersDAOImpl implements UsersDAO {
    @Override
    public boolean save(Users users) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ArrayList<Users> loadDataForTable() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Users users) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ArrayList<Users> filterDataForTable(String input) throws SQLException, ClassNotFoundException {
        return null;
    }
}
