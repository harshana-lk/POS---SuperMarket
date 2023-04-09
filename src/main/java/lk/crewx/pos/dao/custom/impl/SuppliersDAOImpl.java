package lk.crewx.pos.dao.custom.impl;

import lk.crewx.pos.dao.custom.SupplierDAO;
import lk.crewx.pos.model.Suppliers;
import lk.crewx.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class SuppliersDAOImpl implements SupplierDAO {
    @Override
    public boolean save(Suppliers suppliers) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO suppliers(name,address,telephone) VALUES (?,?,?)",
                suppliers.getName(),
                suppliers.getAddress(),
                suppliers.getTelephone()
        );
    }

    @Override
    public ArrayList<Suppliers> loadDataForTable() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM suppliers");
        ArrayList<Suppliers> list = new ArrayList<>();

        while (rst.next()) {
            list.add(new Suppliers(
                    rst.getInt(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            ));
        }
        return list;
    }


    @Override
    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM suppliers WHERE id = ?", id);
    }

    @Override
    public boolean update(Suppliers suppliers) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE suppliers SET name=?, address=?, telephone=? WHERE id=?",
                suppliers.getName(),
                suppliers.getAddress(),
                suppliers.getTelephone(),
                suppliers.getId()
        );
    }

    @Override
    public ArrayList<Suppliers> filterDataForTable(String input) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM suppliers WHERE name like '%" + input + "%' || address like '%" + input + "%'");
        ArrayList<Suppliers> SupplierList = new ArrayList<>();

        while (rst.next()) {
            SupplierList.add(new Suppliers(
                    rst.getInt(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4))
            );
        }
        return SupplierList;
    }

    @Override
    public int getLastSupplierId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT id FROM suppliers ORDER BY id DESC  LIMIT 1");

        while (rst.next()) {
            return rst.getInt(1);
        }
        return -1;
    }

    public ArrayList<Integer> getSupplierId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT Id FROM suppliers");
        ArrayList<Integer> list = new ArrayList<>();

        while (rst.next()) {
            list.add(rst.getInt(1));
        }
        return list;
    }
}
