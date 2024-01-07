package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MapParser {
    public static Map<String,String> parseQueryParameters(HttpExchange exchange) throws IOException {

        URI requestUri = exchange.getRequestURI();
        Map<String, String> parameters = new HashMap<>();
        String query = requestUri.getQuery();

        if (query != null) {
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                    parameters.put(key, value);
                }
            }
        }

        return parameters;
    }
    static private final ObjectMapper objectMapper = new ObjectMapper();
    public static  <T> String parse(T obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }
}
