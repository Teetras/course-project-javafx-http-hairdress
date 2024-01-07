package com.example.demo.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HelloApplication extends Application {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/demo/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 780);
        stage.setTitle("Hair Salon Controller");
        stage.setScene(scene);
        stage.show();

        // Вызов соединения с сервером
        connectToServer();
    }

    public static void main(String[] args) {
        launch();
    }

    private void connectToServer() {
        new Thread(() -> {
            try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Отправляем GET-запрос
                out.println("GET / HTTP/1.1");
                out.println("Host: " + SERVER_HOST);
                out.println();

                // Читаем ответ от сервера
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line).append("\n");
                }

                // Обработка полученного ответа от сервера
                System.out.println("Получен ответ от сервера:");
                System.out.println(response.toString());

                // Здесь вы можете дальше обработать ответ от сервера в соответствии с вашими потребностями
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}