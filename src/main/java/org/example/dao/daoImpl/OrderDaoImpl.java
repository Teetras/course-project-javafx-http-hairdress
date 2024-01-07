package org.example.dao.daoImpl;

import org.example.dao.OrderDao;
import org.example.model.Order;
import org.example.sessionFactory.SessionFactoryImpl;
import org.example.utils.SessionUtils;

import java.util.List;

public class OrderDaoImpl implements OrderDao {

    @Override
    public boolean addOrder(Order order) {
        return SessionUtils.saveEntity(order);
    }

    @Override
    public boolean updateOrder(Order order) {
        return SessionUtils.updateEntity(order);
    }

    @Override
    public boolean deleteOrder(int id) {
        return SessionUtils.deleteEntity(id, Order.class);
    }

    @Override
    public List<Order> getAllOrders() {
        System.out.println((List<Order>)  SessionFactoryImpl.getSessionFactory()
                .openSession()
                .createQuery("FROM Order")
                .list() +" LIST");
        return(List<Order>)SessionFactoryImpl.getSessionFactory()
                .openSession()
                .createQuery("FROM Order")
                .list();
    }

    @Override
    public Order findOrderById(int id) {
        return SessionUtils.find(Order.class, id, "id");
    }
}
