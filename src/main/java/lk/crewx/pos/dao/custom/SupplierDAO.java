package lk.crewx.pos.dao.custom;

import lk.crewx.pos.dao.CrudDAO;
import lk.crewx.pos.model.Suppliers;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SupplierDAO extends CrudDAO<Suppliers,Integer,String> {

    public int getLastSupplierId() throws SQLException, ClassNotFoundException;

    public ArrayList<Integer> getSupplierId() throws SQLException, ClassNotFoundException;


    }
