package org.example.Hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.model.Category;
import org.example.service.CategoryService;
import org.example.service.serviceImpl.CategoryServiceImpl;

import java.io.IOException;
import java.util.List;

public class setCategoriesHendler implements HttpHandler {
    CategoryService serve = new CategoryServiceImpl();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("listCat.toString())");
        try {
            List<Category> listCat = serve.showCategory();
            System.out.println(listCat + "setCategoriesHendler");
            String jsonObj = MapParser.parse(listCat);
            MyHttpSerer.sendResponse(exchange, jsonObj, 200);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MyHttpSerer.sendResponse(exchange, MapParser.parse(new ErrorDto(e.getMessage())), 400);
        }
    }
}
