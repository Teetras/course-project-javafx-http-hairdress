package org.example.Hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.val;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.model.Category;
import org.example.model.HairdressingServices;
import org.example.service.HairdressingServicesServise;
import org.example.service.serviceImpl.HairdressingServicesServiseImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;

public class AddHairdressingServices implements HttpHandler {
    HairdressingServicesServise service = new HairdressingServicesServiseImpl();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            System.out.println("AddHairdressingServices");

            val parameters = MapParser.parseQueryParameters(exchange);
//            System.out.println(parameters);

            String serviceName = parameters.get("serviceNameTextField");
            String description = parameters.get("description");
            String priceString = parameters.get("price");
            double price = Double.parseDouble(priceString);
            String duration = parameters.get("duration");
            String categoryJson = parameters.get("category");

            JSONParser parser = new JSONParser();
            JSONObject categoryJsonObject = (JSONObject) parser.parse(categoryJson);
            Long id = (Long) categoryJsonObject.get("id");
            int idInt = id.intValue();
            String value = (String) categoryJsonObject.get("value");
                     Category category = new Category(idInt, value);
            HairdressingServices hairdressingServices = new HairdressingServices(serviceName, description, price, duration, category);
            System.out.println(hairdressingServices);
            service.addHairdressingServices(hairdressingServices);
            MyHttpSerer.sendResponse(exchange, String.valueOf(true), 200);
        } catch (Exception e) {
            MyHttpSerer.sendResponse(exchange, MapParser.parse(new ErrorDto(e.getMessage())), 400);
        }
    }
}