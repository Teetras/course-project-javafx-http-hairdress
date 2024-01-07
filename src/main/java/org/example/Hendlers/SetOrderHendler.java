package org.example.Hendlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.model.Order;
import org.example.service.OrderService;
import org.example.service.serviceImpl.OrderServiceImpl;

import java.io.IOException;
import java.util.List;

public class SetOrderHendler implements HttpHandler {
    OrderService serve =new OrderServiceImpl();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("List<Order> listOrders 1");
        try {
            List<Order> listOrders = serve.getAllOrders();
            System.out.println(listOrders+ "  listOrders");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(listOrders);
            System.out.println(json+ "  json");

            // Отправка JSON-строки клиенту в качестве ответа HTTP
            MyHttpSerer.sendResponse(exchange, json, 200);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MyHttpSerer.sendResponse(exchange, MapParser.parse(new ErrorDto(e.getMessage())), 400);
        }
    }
}