package org.example.service.serviceImpl;


import org.example.dao.OrderDao;
import org.example.dao.daoImpl.OrderDaoImpl;
import org.example.model.Order;
import org.example.service.OrderService;
import org.hibernate.HibernateError;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    OrderDao orderDao = new OrderDaoImpl();

    @Override
    public boolean addOrder(Order order) {
        boolean isAdded = false;
        try {
            if (orderDao.addOrder(order))
                isAdded = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isAdded;
    }

    @Override
    public boolean updateOrder(Order order) {
        boolean isUpdated = false;
        try {
            if (orderDao.updateOrder(order))
                isUpdated = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteOrder(int id) {
        boolean isDeleted = false;
        try {
            if (orderDao.deleteOrder(id))
                isDeleted = true;
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return isDeleted;
    }

    @Override
    public Order findOrderById(int id) {
        Order order = null;
        try {
            order = orderDao.findOrderById(id);
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = null;
        try {
            orders = orderDao.getAllOrders();
        } catch (HibernateError e) {
            throw new RuntimeException(e);
        }
        return orders;
    }
}