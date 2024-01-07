package com.example.demo.controllers;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.example.demo.GenericService;
import com.example.demo.model.Category;
import com.example.demo.model.HairdressingServiceInfo;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button controllerRecords;

    @FXML
    private Button controllerWorker;
    @FXML
    private Button registerWorker;

    @FXML
    private Button registerServices;

    @FXML
    private TextField fio_field;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private TextField phone_field;
    @FXML
    private Button totalIncome;
    GenericService service = new GenericService.Base();

    private User user;

    @FXML
    public void initialize(User userC) throws ExecutionException {
        user = userC;

        registerServices.setOnAction(actionEvent -> {
            registerServices.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/add-price.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });

    }



    public List<Order> setOrders() {
        System.out.println("http://localhost:8080/setOrders");
        String url = "http://localhost:8080/setOrders";
        Map<String, String> map = new HashMap<>();
        map.put("login", "dscsd");

        try {
            List<Map<String, Object>> orderMapList = service.requestPost(url, map, new TypeReference<List<Map<String, Object>>>() {
            });
            System.out.println(orderMapList + "orderMapList");


            // Преобразование списка Map в список объектов Order
            List<Order> orderList = orderMapList.stream()
                    .map(orderMap -> {
                        Order order = new Order();
                        order.setId((int) orderMap.get("id"));
                        order.setComment((String) orderMap.get("comment"));
                        List<Integer> orderDateList = (List<Integer>) orderMap.get("orderDate");
                        int year = orderDateList.get(0);
                        int month = orderDateList.get(1);
                        int day = orderDateList.get(2);
                        LocalDate orderDate = LocalDate.of(year, month, day);
                        order.setOrderDate(orderDate);

                        Map<String, Object> hairdressingServiceMap = (Map<String, Object>) orderMap.get("hairdressingService");
                        HairdressingServiceInfo hairdressingService = new HairdressingServiceInfo();
                        hairdressingService.setId((int) hairdressingServiceMap.get("id"));
                        hairdressingService.setName((String) hairdressingServiceMap.get("name"));
                        hairdressingService.setDescription((String) hairdressingServiceMap.get("description"));
                        hairdressingService.setDuration((String) hairdressingServiceMap.get("duration"));
                        hairdressingService.setPrice((double) hairdressingServiceMap.get("price"));

                        Map<String, Object> categoryMap = (Map<String, Object>) hairdressingServiceMap.get("category");
                        Category category = new Category();
                        category.setId((int) categoryMap.get("category_id"));
                        category.setValue((String) categoryMap.get("category_name"));
                        hairdressingService.setCategory(category);

                        order.setHairdressingService(hairdressingService);

                        Map<String, Object> userMap = (Map<String, Object>) orderMap.get("user");
                        User user = new User();
                        user.setId(String.valueOf((int) userMap.get("id")));
                        user.setLogin((String) userMap.get("login"));
                        user.setPassword((String) userMap.get("password"));
                        user.setIsWorker(Integer.parseInt((String) userMap.get("isWorker")));
                        user.setIsAdmin(Integer.parseInt((String) userMap.get("isAdmin")));

                        order.setUser(user);
                        System.out.println(order + " order");
                        return order;
                    })
                    .collect(Collectors.toList());

            return orderList;
        } catch (Exception e) {
            // Обработка ошибки
            e.printStackTrace();
            return Collections.emptyList(); // Возвращаем пустой список в случае ошибки
        }

    }

    public double colculate() {
        double totalIncome = 0.0;
        List<Order> allOrders = setOrders();
        for (Order order : allOrders) {
            double serviceCost = order.getHairdressingService().getPrice();
            totalIncome += serviceCost;
        }
        return totalIncome;
    }

    public void handleCreateIncomeReport(MouseEvent mouseEvent) {

        double totalIncome = colculate();


        String filename = "income_report.txt";
        File file = new File(filename);
        String absolutePath = file.getAbsolutePath();
        System.out.println("Путь к файлу: " + absolutePath);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Общая прибыль: " + totalIncome + "$");
            writer.flush();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Отчет о доходах");
            alert.setHeaderText(null);
            alert.setContentText("Общая прибыль сохранена в файл " + filename);
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> setUsers() {
        System.out.println("setUsers");

        String url = "http://localhost:8080/setUsers";
        Map<String, String> map = new HashMap<>();
        map.put("login", "dscsd");

        try {
            List<Map<String, Object>> userMapList = service.requestPost(url, map, new TypeReference<List<Map<String, Object>>>() {
            });
            System.out.println(userMapList + "userList");


            List<User> userList = userMapList.stream()
                    .map(userMap -> new User(userMap.get("id").toString(), (String) userMap.get("login"), (String) userMap.get("password"),
                            parses((String) userMap.get("isWorker")), parses((String) userMap.get("isAdmin"))))
                    .collect(Collectors.toList());
            return userList;
        } catch (Exception e) {
            // Обработка ошибки
            e.printStackTrace();
            return Collections.emptyList(); // Возвращаем пустой список в случае ошибки
        }
    }

    public int parses(String str) {
        System.out.println(str + " str");
        int inn = Integer.parseInt(str);
        return inn;
    }

    public void handleCalculateSalary(MouseEvent mouseEvent) {
        double totalIncome = colculate();
        List<Order> allOrders = setOrders();
        List<User> users = setUsers();
        int orderCount = allOrders.size();
        int workerCount = countWorkers(users); // Подсчитываем количество работников
        double salary = (orderCount / workerCount) * (totalIncome * 0.6);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Заработная плата");
        alert.setHeaderText(null);
        alert.setContentText("Текущая заработная плата сотрудников: " + salary + " $");
        alert.showAndWait();
    }

    private int countWorkers(List<User> users) {
        int workerCount = 0;

        for (User user : users) {
            if (user.getIsWorker() == 1) {
                workerCount++;
            }
        }

        return workerCount;
    }

    public void registerWorker(MouseEvent mouseEvent) {
        String login = login_field.getText();
        String password = password_field.getText();
        String name = fio_field.getText();
        String phone = phone_field.getText();

        if (login.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            System.out.println("Пожалуйста, заполните все поля");
            return;
        }

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Map<String, String> map = new HashMap<>();
                map.put("login", login);
                map.put("password", password);
                map.put("name", name);
                map.put("phone", phone);
                map.put("isWorker", "1");
                Boolean result = service.requestGet("http://localhost:8080/registerWorker", map, new TypeReference<Boolean>() {});
                return result;
            }
        };

        task.setOnSucceeded(event -> {
            Boolean result = task.getValue();
            if (result) {
                System.out.println("Регистрация сотрудника успешна");
                login_field.clear();
                password_field.clear();
                fio_field.clear();
                phone_field.clear();
            } else {
                System.out.println("Ошибка регистрации сотрудника");
            }
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            exception.printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.start();
    }
    public List<Category> setCategoriesList() {
        System.out.println("setCategoriesList");

        String url = "http://localhost:8080/setCategoriesList";
        Map<String, String> map = new HashMap<>();
        map.put("login", "dscsd");

        try {
            List<Map<String, Object>> categoryMapList = service.requestPost(url, map, new TypeReference<List<Map<String, Object>>>() {});
            System.out.println(categoryMapList + "categoryList");

            // Преобразование списка Map в список объектов Category
            List<Category> categoryList = categoryMapList.stream()
                    .map(categoryMap -> new Category((int) categoryMap.get("category_id"), (String) categoryMap.get("category_name")))
                    .collect(Collectors.toList());

            return categoryList;
        } catch (Exception e) {
            // Обработка ошибки
            e.printStackTrace();
            return Collections.emptyList(); // Возвращаем пустой список в случае ошибки
        }
    }

    @FXML
    public void popularCategoryDiagram(ActionEvent event) {
        PopularCategoryDiagram diagram = new PopularCategoryDiagram();
        diagram.start(new Stage());
    }


    public class PopularCategoryDiagram extends Application {

        public void start(Stage primaryStage) {
            List<Order> orders = setOrders();
            System.out.println(orders);
            if (orders == null) {
                System.err.println("Error: Orders list is null.");
                return;
            }

            List<Category> categoryName = setCategoriesList();
            List<Category> categoriesUsed = new ArrayList<>();

            for (Order order : orders) {
                Category category = order.getHairdressingService().getCategory();
                categoriesUsed.add(category);
            }

            Map<String, Integer> categoryCounts = new HashMap<>();
            for (Category category : categoriesUsed) {
                String fieldName = category.getValue();
                categoryCounts.put(fieldName, categoryCounts.getOrDefault(fieldName, 0) + 1);
            }
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
                String fieldName = entry.getKey();
                int count = entry.getValue();
                PieChart.Data data = new PieChart.Data(fieldName, count);
                pieChartData.add(data);
            }

            PieChart pieChart = new PieChart(pieChartData);
            pieChart.setTitle("Диаграмма популярных категорий");

            // Настройка автоматического изменения размеров диаграммы
            pieChart.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            pieChart.setPrefSize(800, 600);
            pieChart.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            // Создание контейнера StackPane и добавление диаграммы в него
            StackPane root = new StackPane(pieChart);

            // Создание сцены и отображение диаграммы
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Диаграмма популярных категорий");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        public static void main(String[] args) {
            launch(args);
        }
        public List<Order> setOrders() {
            System.out.println("http://localhost:8080/setOrders");
            String url = "http://localhost:8080/setOrders";
            Map<String, String> map = new HashMap<>();
            map.put("login", "dscsd");

            try {
                List<Map<String, Object>> orderMapList = service.requestPost(url, map, new TypeReference<List<Map<String, Object>>>() {
                });
                System.out.println(orderMapList + "orderMapList");


                // Преобразование списка Map в список объектов Order
                List<Order> orderList = orderMapList.stream()
                        .map(orderMap -> {
                            Order order = new Order();
                            order.setId((int) orderMap.get("id"));
                            order.setComment((String) orderMap.get("comment"));
                            List<Integer> orderDateList = (List<Integer>) orderMap.get("orderDate");
                            int year = orderDateList.get(0);
                            int month = orderDateList.get(1);
                            int day = orderDateList.get(2);
                            LocalDate orderDate = LocalDate.of(year, month, day);
                            order.setOrderDate(orderDate);

                            Map<String, Object> hairdressingServiceMap = (Map<String, Object>) orderMap.get("hairdressingService");
                            HairdressingServiceInfo hairdressingService = new HairdressingServiceInfo();
                            hairdressingService.setId((int) hairdressingServiceMap.get("id"));
                            hairdressingService.setName((String) hairdressingServiceMap.get("name"));
                            hairdressingService.setDescription((String) hairdressingServiceMap.get("description"));
                            hairdressingService.setDuration((String) hairdressingServiceMap.get("duration"));
                            hairdressingService.setPrice((double) hairdressingServiceMap.get("price"));

                            Map<String, Object> categoryMap = (Map<String, Object>) hairdressingServiceMap.get("category");
                            Category category = new Category();
                            category.setId((int) categoryMap.get("category_id"));
                            category.setValue((String) categoryMap.get("category_name"));
                            hairdressingService.setCategory(category);

                            order.setHairdressingService(hairdressingService);

                            Map<String, Object> userMap = (Map<String, Object>) orderMap.get("user");
                            User user = new User();
                            user.setId(String.valueOf((int) userMap.get("id")));
                            user.setLogin((String) userMap.get("login"));
                            user.setPassword((String) userMap.get("password"));
                            user.setIsWorker(Integer.parseInt((String) userMap.get("isWorker")));
                            user.setIsAdmin(Integer.parseInt((String) userMap.get("isAdmin")));

                            order.setUser(user);
                            System.out.println(order + " order");
                            return order;
                        })
                        .collect(Collectors.toList());

                return orderList;
            } catch (Exception e) {
                // Обработка ошибки
                e.printStackTrace();
                return Collections.emptyList(); // Возвращаем пустой список в случае ошибки
            }

        }


    }
}