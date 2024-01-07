package org.example.Hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.val;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.model.Category;
import org.example.model.HairdressingServices;
import org.example.model.Order;
import org.example.model.User;
import org.example.service.OrderService;
import org.example.service.serviceImpl.OrderServiceImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.time.LocalDate;

//дописать добавление
public class AddOrderHandler implements HttpHandler {

    OrderService serve = new OrderServiceImpl();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("AddOrderHandler");
        try {
            val parameters = MapParser.parseQueryParameters(exchange);
            System.out.println(parameters + "  dddddd");

            // Получение JSON-строки из параметров запроса
            String hairdressingJsonString = parameters.get("hairdressing");
            String categoryJsonString = parameters.get("category");
            String userJsonString = parameters.get("userJsonString");
            String comment = parameters.get("comment");

            // Распарсинг hairdressing
            JSONObject hairdressingJson = (JSONObject) new JSONParser().parse(hairdressingJsonString);
            String duration = (String) hairdressingJson.get("duration");
            String price = (String) hairdressingJson.get("price");
            double doublePrice = Double.parseDouble(price);
            long idHair = (long) hairdressingJson.get("id");
            int idIntHair = Integer.parseInt(String.valueOf(idHair));
            String name = (String) hairdressingJson.get("name");
            String description = (String) hairdressingJson.get("description");

            // Распарсинг category
            JSONObject categoryJsonObject = (JSONObject) new JSONParser().parse(categoryJsonString);
            Long id = (Long) categoryJsonObject.get("id");
            int idInt = id.intValue();
            String value = (String) categoryJsonObject.get("value");
            System.out.println("idInt  " + idInt);

            // Распарсинг user
            JSONObject userJson = (JSONObject) new JSONParser().parse(userJsonString);
            System.out.println("userJson  " + userJson);

            String userLogin = (String) userJson.get("userLogin");
            String userPassword = (String) userJson.get("userPassword");
            String userId = (String) userJson.get("userId");
            int intUsrId = Integer.parseInt(userId);
            long userIsAdmin = (long) userJson.get("userIsAdmin");

            String isAdmin = String.valueOf(userIsAdmin);
            long userIsWorker = (long) userJson.get("userIsWorker");
            String isWorker = String.valueOf(userIsWorker);
            String orderDateString = parameters.get("orderDate");
            LocalDate orderDate = LocalDate.parse(orderDateString);
            // Создание объектов HairdressingServices, Category и User
            Category category = new Category(idInt, value);
            HairdressingServices hairdressingServices = new HairdressingServices(idIntHair, name, description, doublePrice, duration, category);

            User user = new User(intUsrId, userLogin, userPassword, isWorker, isAdmin);


            // Создание объекта Order и установка связей
            Order order = new Order(user, hairdressingServices, comment,orderDate);
            // Добавление Order в базу данных через serve.addOrder()
            serve.addOrder(order);
            MyHttpSerer.sendResponse(exchange, String.valueOf(true), 200);
        } catch (Exception e) {

            MyHttpSerer.sendResponse(exchange, MapParser.parse(new ErrorDto(e.getMessage())), 400);
        }
    }
}
