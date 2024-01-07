package org.example.dao;

import org.example.model.Order;

import java.util.List;

public interface OrderDao {
    boolean addOrder(Order order);
    boolean updateOrder(Order order);
    boolean deleteOrder(int id);
    List<Order> getAllOrders();
    Order findOrderById(int id);
}