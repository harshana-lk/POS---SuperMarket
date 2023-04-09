package lk.crewx.pos.dao;

import lk.crewx.pos.dao.custom.DaoTypes;
import lk.crewx.pos.dao.custom.impl.ItemDAOImpl;
import lk.crewx.pos.dao.custom.impl.OrdersDAOImpl;

public class FactoryDAO {
    private static FactoryDAO daoFactory;

    private FactoryDAO() {
    }

    public static FactoryDAO getInstance() {
        return daoFactory == null ? (daoFactory = new FactoryDAO()) : daoFactory;
    }

    public <T> T getDao(DaoTypes type) {
        switch (type) {
            case ITEM:
                return (T) new ItemDAOImpl();
            case ORDERS:
                return (T) new OrdersDAOImpl();
            default:
                return null;
        }
    }
}
