package org.example.Hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.val;
import org.example.service.CategoryService;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.model.Category;
import org.example.service.serviceImpl.CategoryServiceImpl;

import java.io.IOException;

public class AddCategoryHandler implements HttpHandler {

CategoryService serve = new CategoryServiceImpl();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("AddCategoryHandler");
        try{
            val parameters = MapParser.parseQueryParameters(exchange);
            System.out.println(parameters);

            serve.addCategory(new Category(parameters.get("category_name")));
            MyHttpSerer.sendResponse(exchange, String.valueOf(true), 200);
        }catch (Exception e){

            MyHttpSerer.sendResponse(exchange,MapParser.parse(new ErrorDto(e.getMessage())),400);
        }
    }
}
