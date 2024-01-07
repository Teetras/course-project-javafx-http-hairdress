package org.example.Hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.val;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.model.User;
import org.example.service.PersonService;
import org.example.service.UserService;
import org.example.service.serviceImpl.PersonServiceImpl;
import org.example.service.serviceImpl.UserServiceImpl;

import java.io.IOException;

public class RegisterWorkerHendler implements HttpHandler {

    UserService serve = new UserServiceImpl();
    PersonService person=new PersonServiceImpl();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            val parameters = MapParser.parseQueryParameters(exchange);
            System.out.println(parameters);
            User user=new User( parameters.get("login"), parameters.get("password"),parameters.get("isWorker"));
            serve.addUser(user);
//            person.addPerson(new Person(parameters.get("name"), parameters.get("phone"),user));
            MyHttpSerer.sendResponse(exchange, String.valueOf(true), 200);
        } catch (Exception e) {
            MyHttpSerer.sendResponse(exchange, MapParser.parse(new ErrorDto(e.getMessage())), 400);
        }
    }
}