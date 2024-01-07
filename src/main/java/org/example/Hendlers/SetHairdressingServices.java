package org.example.Hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.model.HairdressingServices;
import org.example.service.HairdressingServicesServise;
import org.example.service.serviceImpl.HairdressingServicesServiseImpl;

import java.io.IOException;
import java.util.List;

public class SetHairdressingServices implements HttpHandler {
    HairdressingServicesServise serve =new HairdressingServicesServiseImpl();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
            System.out.println("listHairdressingServices 1");
            try {
                List<HairdressingServices> listHairdressingServices = serve.showHairdressingServices();
                String jsonObj = MapParser.parse(listHairdressingServices);
                MyHttpSerer.sendResponse(exchange, jsonObj, 200);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                MyHttpSerer.sendResponse(exchange, MapParser.parse(new ErrorDto(e.getMessage())), 400);
            }
   }
}

