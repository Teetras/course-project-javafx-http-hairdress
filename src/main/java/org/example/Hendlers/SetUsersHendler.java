package org.example.Hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.model.User;
import org.example.service.UserService;
import org.example.service.serviceImpl.UserServiceImpl;

import java.io.IOException;
import java.util.List;

public class SetUsersHendler implements HttpHandler {
    UserService serve = new UserServiceImpl();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            List<User> users = serve.showPeople();
            String jsonObj = MapParser.parse(users);
            MyHttpSerer.sendResponse(exchange, jsonObj, 200);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MyHttpSerer.sendResponse(exchange, MapParser.parse(new ErrorDto(e.getMessage())), 400);
        }
    }
}
