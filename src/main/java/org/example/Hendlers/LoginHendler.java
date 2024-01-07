package org.example.Hendlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.val;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.model.User;
import org.example.service.UserService;
import org.example.service.serviceImpl.UserServiceImpl;

import java.io.IOException;

public class LoginHendler implements HttpHandler {
    UserService serve = new UserServiceImpl();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            exchange.getRequestMethod().equals("GET");
            val parameters = MapParser.parseQueryParameters(exchange);
            System.out.println(parameters);
            serve.login(parameters.get("login"), parameters.get("password"));
            User user = serve.login(parameters.get("login"), parameters.get("password"));
            ObjectMapper objectMapper = new ObjectMapper(); // Используем Jackson
            System.out.println(user.getId());
            String userJson = objectMapper.writeValueAsString(user);
            System.out.println(userJson + "  json");
            MyHttpSerer.sendResponse(exchange, userJson, 200);
        } catch (Exception e) {
            MyHttpSerer.sendResponse(exchange, MapParser.parse(new ErrorDto(e.getMessage())), 400);
        }
    }
}
