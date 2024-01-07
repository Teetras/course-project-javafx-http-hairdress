package org.example.service;

import org.example.model.Order;

import java.util.List;

public interface OrderService {
    boolean addOrder(Order order);
    boolean updateOrder(Order order);
    boolean deleteOrder(int id);
    Order findOrderById(int id);
    List<Order> getAllOrders();
}
