package org.example.Hendlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.val;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.model.Person;
import org.example.model.User;
import org.example.service.PersonService;
import org.example.service.UserService;
import org.example.service.serviceImpl.PersonServiceImpl;
import org.example.service.serviceImpl.UserServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FindPersonHandler implements HttpHandler {
    PersonService service = new PersonServiceImpl();
    UserService users=new UserServiceImpl();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<User> users1 =users.showPeople();
        try {
            System.out.println("persooon");
            val parameters = MapParser.parseQueryParameters(exchange);
            System.out.println(parameters + "  parameters");
            String userId = parameters.get("idd");
            List<Person> personList = service.getAllPersons();
            // Проверка наличия параметра "id"
            if (userId == null) {
                MyHttpSerer.sendResponse(exchange, "Missing 'id' parameter", 400);
                return;
            }

            int id;
            try {
                id = Integer.parseInt(userId);
            } catch (NumberFormatException e) {
                MyHttpSerer.sendResponse(exchange, "Invalid 'id' parameter", 400);
                return;
            }

            List<User> filteredUserList = users1.stream()
                    .filter(person -> person.getId() == id)
                    .collect(Collectors.toList());
            Person pers = filteredUserList.get(0).getPerson_id();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String obj = objectMapper.writeValueAsString(pers);
            System.out.println(obj + " obj");
            MyHttpSerer.sendResponse(exchange, obj, 200);
        } catch (Exception e) {
            MyHttpSerer.sendResponse(exchange, MapParser.parse(new ErrorDto(e.getMessage())), 400);
        }
    }


}
