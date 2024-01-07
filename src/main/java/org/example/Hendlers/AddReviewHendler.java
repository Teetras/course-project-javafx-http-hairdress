package org.example.Hendlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.val;
import org.example.ErrorDto;
import org.example.MapParser;
import org.example.MyHttpSerer;
import org.example.model.Order;
import org.example.model.Review;
import org.example.service.OrderService;
import org.example.service.ReviewService;
import org.example.service.serviceImpl.OrderServiceImpl;
import org.example.service.serviceImpl.ReviewServiceImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddReviewHendler implements HttpHandler {
    ReviewService serve= new ReviewServiceImpl();
    OrderService order = new OrderServiceImpl();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            val parameters = MapParser.parseQueryParameters(exchange);
            System.out.println(parameters + "  dddddd");
            String comment = parameters.get("comment");
            int rating = Integer.parseInt(parameters.get("rating"));
            String dateString = parameters.get("date");
            int orderId = Integer.parseInt(parameters.get("idOrder"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);
            Review review = new Review();
            review.setComment(comment);
            review.setRating(rating);
            review.setReviewDate(date);
            Order order1=order.findOrderById(orderId);
            review.setOrder(order1);
            serve.addReview(review);
            MyHttpSerer.sendResponse(exchange, String.valueOf(true), 200);
        } catch (Exception e) {

            MyHttpSerer.sendResponse(exchange, MapParser.parse(new ErrorDto(e.getMessage())), 400);
        }
    }
    }

