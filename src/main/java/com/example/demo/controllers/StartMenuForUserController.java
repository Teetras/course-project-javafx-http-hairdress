package com.example.demo.controllers;

import com.example.demo.GenericService;
import com.example.demo.ServiceDetailsDialog;
import com.example.demo.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Getter
@Setter
public class StartMenuForUserController {

    private ResourceBundle resources;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private URL location;

    @FXML
    private TableView<HairdressingServiceInfo> tableView;

    GenericService service = new GenericService.Base();
    private User user;
    @FXML
    private Person person;

    @FXML
    void initialize(User userC) throws ExecutionException {
        user = userC;
        System.out.println(user.getId()+"  user.getId()");

        person=findPerson(Integer.parseInt(user.getId()));
        System.out.println(person);
        System.out.println("StartMenuForUserController " + user);
        populateTable(setHairdressingServices());
    }

    public List<HairdressingServiceInfo> setHairdressingServices() throws ExecutionException {
        Task<List<HairdressingServiceInfo>> task = new Task<List<HairdressingServiceInfo>>() {
            @Override
            protected List<HairdressingServiceInfo> call() throws Exception {
                System.out.println("setHairdressingServices Task");

                String url = "http://localhost:8080/setHairdressingServices";
                Map<String, String> map = new HashMap<>();
                map.put("login", "dscsd");
                try {
                    List<Map<String, Object>> hairdressingServiceList = service.requestGet(url, map, new TypeReference<List<Map<String, Object>>>() {
                    });

                    // Преобразование списка JSON-объектов в список объектов HairdressingServiceInfo
                    List<HairdressingServiceInfo> hairdressingServices = hairdressingServiceList.stream()
                            .map(this::mapToHairdressingServiceInfo)
                            .collect(Collectors.toList());

                    System.out.println(hairdressingServices + " в трай кетч еще");
                    return hairdressingServices;
                } catch (Exception e) {
                    // Обработка ошибки
                    e.printStackTrace();
                    throw new RuntimeException("Ошибка при получении услуг парикмахерской"); // выбрасываем исключение в случае ошибки
                }
            }

            private HairdressingServiceInfo mapToHairdressingServiceInfo(Map<String, Object> hairdressingService) {
                // Извлечение значений из JSON-объекта и создание объекта HairdressingServiceInfo
                int id = (int) hairdressingService.get("id");
                String name = (String) hairdressingService.get("name");
                String description = (String) hairdressingService.get("description");
                String duration = (String) hairdressingService.get("duration");
                double price = (double) hairdressingService.get("price");

                Map<String, Object> categoryMap = (Map<String, Object>) hairdressingService.get("category");
                int categoryId = (int) categoryMap.get("category_id");
                String categoryName = (String) categoryMap.get("category_name");

                Category categoryInfo = new Category();
                categoryInfo.setId(categoryId);
                categoryInfo.setValue(categoryName);

                HairdressingServiceInfo hairdressingServiceInfo = new HairdressingServiceInfo();
                hairdressingServiceInfo.setId(id);
                hairdressingServiceInfo.setName(name);
                hairdressingServiceInfo.setDescription(description);
                hairdressingServiceInfo.setDuration(duration);
                hairdressingServiceInfo.setPrice(price);
                hairdressingServiceInfo.setCategory(categoryInfo);

                return hairdressingServiceInfo;
            }
        };


        Thread thread = new Thread(task);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Получение результата задачи
        try {
            System.out.println(task.get() + " return");
            return task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void populateTable(List<HairdressingServiceInfo> data) {
        // Очистка таблицы перед заполнением
        tableView.getItems().clear();

        ObservableList<HairdressingServiceInfo> tableData = FXCollections.observableArrayList(data);

        // Получение столбцов таблицы
        TableColumn<HairdressingServiceInfo, String> serviceNameColumn = (TableColumn<HairdressingServiceInfo, String>) tableView.getColumns().get(0);
        TableColumn<HairdressingServiceInfo, String> descriptionColumn = (TableColumn<HairdressingServiceInfo, String>) tableView.getColumns().get(1);
        TableColumn<HairdressingServiceInfo, String> serviceTypeColumn = (TableColumn<HairdressingServiceInfo, String>) tableView.getColumns().get(2);
        TableColumn<HairdressingServiceInfo, String> durationColumn = (TableColumn<HairdressingServiceInfo, String>) tableView.getColumns().get(3);
        TableColumn<HairdressingServiceInfo, String> costColumn = (TableColumn<HairdressingServiceInfo, String>) tableView.getColumns().get(4);

        // Привязка данных к столбцам таблицы
        serviceNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        serviceTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getValue()));
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDuration()));
        costColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrice())));

        // Установка данных в таблицу
        tableView.setItems(tableData);
    }
    public Person findPerson(int id) {
        System.out.println("findPerson "+ String.valueOf(id));
String idStr= String.valueOf(id);
        String url = "http://localhost:8080/findPerson";
        Map<String, String> map = new HashMap<>();
        map.put("idd", idStr);
        map.put("cho", "cho");
        System.out.println("map "+ map);
        try {
            List<Map<String, Object>> personList = service.requestGet(url, map, new TypeReference<List<Map<String, Object>>>() {});
            if (!personList.isEmpty()) {
                Map<String, Object> personMap = personList.get(0);
                System.out.println(personMap + "person");

                // Преобразование Map в объект Person
                int personId = (int) personMap.get("id");
                String name = (String) personMap.get("name");
                String phone = (String) personMap.get("phone");

                return new Person(personId, name, phone);
            }
        } catch (Exception e) {
            // Обработка ошибки
            e.printStackTrace();
            return null; // Возвращаем null в случае ошибки
        }
        return null;
    }


    @FXML
    void onTableItemClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // Проверяем двойной клик
            HairdressingServiceInfo selectedService = tableView.getSelectionModel().getSelectedItem();
            if (selectedService != null) {
                ServiceDetailsDialog<Map<String, Object>> dialog = new ServiceDetailsDialog<>(selectedService, user.getPerson_id().getName());
                Optional<Map<String, Object>> result = dialog.showAndWait();

                // Проверяем, был ли выбран сервис для записи
                if (result.isPresent()) {
                    Map<String, Object> resultMap = result.get();
                    HairdressingServiceInfo selectedServiceForBooking = (HairdressingServiceInfo) resultMap.get("service");
                    String comment = (String) resultMap.get("comment");
                    System.out.println(selectedServiceForBooking.getName() + "    selectedServiceForBooking");
                    System.out.println(comment + "    comment");
                    addOrder(selectedServiceForBooking, comment);
                }
            }
        }
    }

    public void addOrder(HairdressingServiceInfo serviceForBooking, String comment) {

        Category category = serviceForBooking.getCategory();
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Map<String, String> map = new HashMap<>();
                JSONObject hairJson = new JSONObject();

                LocalDate currentDate = LocalDate.now();

                map.put("orderDate", currentDate.toString());
                hairJson.put("name", serviceForBooking.getName());
                hairJson.put("id", serviceForBooking.getId());
                hairJson.put("description", serviceForBooking.getDescription());
                hairJson.put("duration", serviceForBooking.getDuration());
                hairJson.put("price", String.valueOf(serviceForBooking.getPrice()));
                String hairJsonString = hairJson.toJSONString();
                map.put("hairdressing", hairJsonString);

                JSONObject categoryJson = new JSONObject();
                categoryJson.put("id", category.getId());
                categoryJson.put("value", category.getValue());
                String categoryJsonString = categoryJson.toJSONString();
                System.out.println(categoryJsonString + " categoryJson");
                map.put("category", categoryJsonString);

                JSONObject userJson = new JSONObject();
                userJson.put("userId", user.getId());

                userJson.put("userLogin", user.getLogin());

                userJson.put("userPassword", user.getPassword());
                userJson.put("userIsAdmin", user.getIsAdmin());
                userJson.put("userIsWorker", user.getIsWorker());
                String userJsonString = userJson.toJSONString();
                map.put("userJsonString", userJsonString);

                map.put("comment", comment);
                System.out.println("order " + map);
                return service.requestGet("http://localhost:8080/addOrder", map, new TypeReference<Boolean>() {
                });
            }
        };
        task.setOnSucceeded(workerStateEvent -> {
            System.out.println("setOnSucceeded");
            if (!task.getValue().booleanValue()) return;
            String successMessage = "Заказ успешно оформлен.";
            showSuccessNotification(successMessage);
            System.out.println("addCategory Task cool");
        });
        task.setOnFailed(workerStateEvent -> {
            System.out.println(task.getException().getMessage());
        });
        new Thread(task).start();
    }

    private void showSuccessNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Уведомление");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void openPerson(ActionEvent actionEvent) {
        Node sourceNode = (Node) actionEvent.getSource();
        Window currentWindow = sourceNode.getScene().getWindow();

        progressIndicator.setVisible(true);

        Task<Pair<Parent, PersonController>> task = new Task<Pair<Parent, PersonController>>() {
            @Override
            protected Pair<Parent, PersonController> call() throws Exception {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/demo/person.fxml"));
                System.out.println("ok1");

                try {
                    Parent root = loader.load();
                    PersonController controller = loader.getController();
                    controller.initialize(user);
                    System.out.println("ok2");


                    updateProgress(1, 1); // Устанавливаем значение прогресса в 100% после выполнения работы

                    return new Pair<>(root, controller);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            currentWindow.hide();
            Pair<Parent, PersonController> result = task.getValue();
            Parent root = result.getKey();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            System.out.println("ok");
            // Установка окна сцены неразделимым
            stage.setResizable(false);
            stage.show();

            progressIndicator.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    public void sortByPrice(ActionEvent actionEvent) throws ExecutionException {
        List<HairdressingServiceInfo> data= setHairdressingServices();

        Comparator<HairdressingServiceInfo> priceComparator = Comparator.comparingDouble(HairdressingServiceInfo::getPrice);
        Collections.sort(data, priceComparator);
        populateTable(data);
    }

    public void sortByAlphabet(ActionEvent actionEvent) throws ExecutionException {
        List<HairdressingServiceInfo> data = setHairdressingServices();
        Comparator<HairdressingServiceInfo> nameComparator = Comparator.comparing(HairdressingServiceInfo::getName);
        Collections.sort(data, nameComparator);
        populateTable(data);

    }

    public void filterByPrice(ActionEvent actionEvent) {

    }
}
