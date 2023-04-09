package lk.crewx.pos.db;

import lk.crewx.pos.util.CrudUtil;

import java.sql.SQLException;

public class BalanceUpdating {
    public static boolean addToBalance(double i) {
        try {
            return CrudUtil.execute("UPDATE money SET balance=(balance+?) WHERE id=0", i);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean removeBaance(double i) {
        try {
            return CrudUtil.execute("UPDATE money SET balance=(balance-?) WHERE id=0", i);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
