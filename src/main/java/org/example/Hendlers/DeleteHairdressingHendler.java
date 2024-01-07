package org.example.Hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.val;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.service.HairdressingServicesServise;
import org.example.service.serviceImpl.HairdressingServicesServiseImpl;

import java.io.IOException;

public class DeleteHairdressingHendler implements HttpHandler {
    HairdressingServicesServise service = new HairdressingServicesServiseImpl();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            System.out.println("DELHairdressingServices");

            val parameters = MapParser.parseQueryParameters(exchange);

            String serviceid = parameters.get("id");

            int serviceidInt=Integer.parseInt(serviceid);
            System.out.println(serviceidInt+ " serviceidInt");
            service.deleteHairdressingServices(serviceidInt);
            MyHttpSerer.sendResponse(exchange, String.valueOf(true), 200);
        } catch (Exception e) {
            MyHttpSerer.sendResponse(exchange, MapParser.parse(new ErrorDto(e.getMessage())), 400);
        }
    }
}
