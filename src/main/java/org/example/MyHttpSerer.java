package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.example.Hendlers.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MyHttpSerer {
    public static void main(String[] args) throws IOException {
        int port=8080;
        HttpServer  server = HttpServer.create(new InetSocketAddress(port),0);
server.createContext("/login",new LoginHendler());
        server.createContext("/register",new RegisterHendler());
        server.createContext("/addCategory",new AddCategoryHandler());
        server.createContext("/setCategoriesList",new setCategoriesHendler());
        server.createContext("/addHairdressingServices",new AddHairdressingServices());
        server.createContext("/setHairdressingServices",new SetHairdressingServices());
        server.createContext("/setOrders",new SetOrderHendler());
        server.createContext("/addOrder",new AddOrderHandler());
        server.createContext("/updateHairdressing",new UpdateHairdressingHandler());
        server.createContext("/deleteHairdressing",new DeleteHairdressingHendler());
        server.createContext("/findPerson",new FindPersonHandler());
        server.createContext("/setUsers",new SetUsersHendler());
        server.createContext("/addReview",new AddReviewHendler());
        server.createContext("/registerWorker",new RegisterWorkerHendler());

        server.setExecutor(null);
        server.start();
        System.out.println("Server start in port ");
    }
    public static void sendResponse(HttpExchange exchange, String response,int code) throws IOException {
        OutputStream os = exchange.getResponseBody();
        if(!response.isEmpty()){
            exchange.getResponseHeaders().set("Content-Type","application/json");
            exchange.sendResponseHeaders(code,response.getBytes("UTF-8").length);
            os.write(response.getBytes());
        }else{
            exchange.sendResponseHeaders(code,-1);
        }
        os.close();
    }
   /* private static DateBaseHandler dbHandler;

    public static void main(String[] args) {
        int port = 8080; // Порт, на котором будет запущен сервер

        // Создаем экземпляр DateBaseHandler при запуске сервера
        dbHandler = new DateBaseHandler();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Получено новое подключение: " + clientSocket);
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            System.out.println(line); // Выводим полученный HTTP-запрос
        }

        // Выполняем операции с базой данных, используя экземпляр DateBaseHandler
        if (dbHandler.checkConnection()) {
            // База данных подключена, выполните нужные операции
            System.out.println("Подключение к базе данных успешно.");
            // Например, вызовите метод signUpUser() для регистрации пользователя
//            dbHandler.signUpUser("John Doe", "123456789", "johndoe", "password");
        } else {
            // Ошибка подключения к базе данных
            System.out.println("Ошибка подключения к базе данных.");
        }

        // Отправляем HTTP-ответ
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println();
        out.println("<html><body>");
        out.println("<h1>Привет, мир!</h1>");
        out.println("</body></html>");

        out.close();
        in.close();
        clientSocket.close();
    }*/
}