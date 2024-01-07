package com.example.demo.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.example.demo.GenericService;
import com.example.demo.model.Category;
import com.example.demo.model.HairdressingServiceInfo;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class addPriceController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addButton;

    @FXML
    private Button addCategory;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextField categoryName;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField durationTextField;

    @FXML
    private Label priceLabel;

    @FXML
    private Slider priceSlider;

    @FXML
    private TextField serviceNameTextField;

    @FXML
    private TableView<HairdressingServiceInfo> tableView;


    GenericService service = new GenericService.Base();
    private HairdressingServiceInfo selectedService=null;
    @FXML
    public void initialize() throws ExecutionException, InterruptedException {
        // Установка начального значения в Label, соответствующего начальному положению ползунка
        priceLabel.setText(String.format("%.2f", priceSlider.getValue()));

        // Обработка события изменения значения ползунка
        priceSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Обновление значения в Label при изменении положения ползунка
            priceLabel.setText(String.format("%.2f", newValue.doubleValue()));
        });
//        populateComboBox(setCategoriesList());
        loadCategories();
        populateTable(setHairdressingServices());

        // Установка слушателя для выбора строки в таблице
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedService = newValue;

//            if (selectedService != null) {
//                System.out.println("Selected Service:");
//                System.out.println("ID: " + selectedService.getId());
//                System.out.println("Name: " + selectedService.getName());
//                System.out.println("Description: " + selectedService.getDescription());
//                System.out.println("Duration: " + selectedService.getDuration());
//                System.out.println("Price: " + selectedService.getPrice());
//                System.out.println("Category: " + selectedService.getCategory().getValue());
//            }
        });

    }

    public void delete(ActionEvent actionEvent) {
        if (selectedService != null) {
//            HairdressingServiceInfo editedElement = editDialog(selectedService, setCategoriesList());
//            System.out.println(editedElement);
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println("addHairdressingServices 2222");
                    Map<String, String> map = new HashMap<>();
                    map.put("id", String.valueOf(selectedService.getId()));
                    map.put("serviceNameTextField", selectedService.getName());
                    map.put("description", selectedService.getDescription());
                    map.put("duration", selectedService.getDuration());
                    map.put("price", String.valueOf(selectedService.getPrice()));
                    Category selectedCategoryName = selectedService.getCategory();
                    JSONObject categoryJson = new JSONObject();
                    categoryJson.put("id", selectedCategoryName.getId());
                    categoryJson.put("value", selectedCategoryName.getValue());
                    String categoryJsonString = categoryJson.toJSONString();
                    System.out.println(categoryJsonString + " categoryJson");
//                    map.put("category", categoryJsonString);
                    System.out.println(map + " map");
                    return service.requestGet("http://localhost:8080/deleteHairdressing", map, new TypeReference<Boolean>() {

                    });
                }
            };
            new Thread(task).start();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Элемент не выбран");
            alert.showAndWait();
        }

    }

    public void editHairdressing(ActionEvent actionEvent) {
        if (selectedService != null) {
            HairdressingServiceInfo editedElement = editDialog(selectedService, setCategoriesList());
                System.out.println(editedElement);
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println("addHairdressingServices 2222");
                    Map<String, String> map = new HashMap<>();
                    map.put("id", String.valueOf(selectedService.getId()));
                    map.put("serviceNameTextField", selectedService.getName());
                    map.put("description", selectedService.getDescription());
                    map.put("duration", selectedService.getDuration());
                    map.put("price", String.valueOf(selectedService.getPrice()));
                    Category selectedCategoryName = selectedService.getCategory();
                    JSONObject categoryJson = new JSONObject();
                    categoryJson.put("id", selectedCategoryName.getId());
                    categoryJson.put("value", selectedCategoryName.getValue());
                    String categoryJsonString = categoryJson.toJSONString();
                    System.out.println(categoryJsonString + " categoryJson");

                    map.put("category", categoryJsonString);



                    System.out.println(map + " map");
                    return service.requestGet("http://localhost:8080/updateHairdressing", map, new TypeReference<Boolean>() {
                    });
                }
            };
            new Thread(task).start();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText(null);
            alert.setContentText("Элемент не выбран");
            alert.showAndWait();
        }
    }
    public HairdressingServiceInfo editDialog(HairdressingServiceInfo serviceInfo, List<Category> categoryList) {
        TextField nameTextField = new TextField(serviceInfo.getName());
        TextField descriptionTextField = new TextField(serviceInfo.getDescription());
        TextField priceTextField = new TextField(String.valueOf(serviceInfo.getPrice()));

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryList.stream()
                .map(Category::getValue)
                .collect(Collectors.toList())));
        categoryComboBox.setValue(serviceInfo.getCategory().getValue());


        Dialog<HairdressingServiceInfo> dialog = new Dialog<>();
        dialog.setTitle("Редактирование сервиса");
        dialog.setHeaderText("Редактирование сервиса: " + serviceInfo.getName());

        // Создание кнопок "ОК" и "Отмена"
        ButtonType okButtonType = new ButtonType("ОК", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Создание сетки для размещения элементов
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Добавление текстовых полей и комбобокса в сетку
        gridPane.add(new Label("Название:"), 0, 0);
        gridPane.add(nameTextField, 1, 0);
        gridPane.add(new Label("Описание:"), 0, 1);
        gridPane.add(descriptionTextField, 1, 1);
        gridPane.add(new Label("Цена:"), 0, 2);
        gridPane.add(priceTextField, 1, 2);
        gridPane.add(new Label("Категория:"), 0, 3);
        gridPane.add(categoryComboBox, 1, 3);

        // Добавление сетки в диалоговое окно
        dialog.getDialogPane().setContent(gridPane);

        // Установка результатов диалогового окна при нажатии кнопки "ОК"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                // Создание нового объекта HairdressingServiceInfo с обновленными значениями
                HairdressingServiceInfo updatedServiceInfo = serviceInfo;
                updatedServiceInfo.setName(nameTextField.getText());
                updatedServiceInfo.setDescription(descriptionTextField.getText());
                updatedServiceInfo.setPrice(Double.parseDouble(priceTextField.getText()));

                String categoryName = categoryComboBox.getValue();
                Category selectedCategory = categoryList.stream()
                        .filter(category -> category.getValue().equals(categoryName))
                        .findFirst()
                        .orElse(null);

                updatedServiceInfo.setCategory(selectedCategory);
                return updatedServiceInfo;
            }
            return null;
        });

        // Отображение диалогового окна и ожидание результата
        Optional<HairdressingServiceInfo> result = dialog.showAndWait();

        // Возвращение обновленного объекта HairdressingServiceInfo или null, если нажата кнопка "Отмена"
        return result.orElse(null);
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
                    List<Map<String, Object>> hairdressingServiceList = service.requestPost(url, map, new TypeReference<List<Map<String, Object>>>() {
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

        // Запуск задачи и ожидание завершения
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
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedService = newValue;
        });

    }


    public void addCategory() {
        if (categoryName.equals("")) {
            // Если поле ввода пустое, выводим сообщение об ошибке
            System.out.println("Введите название категории");
            return;
        }
        System.out.println("addCategory");
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Map<String, String> map = new HashMap<>();

                map.put("category_name", categoryName.getText());

                System.out.println("addCategory Task");
                return service.requestGet("http://localhost:8080/addCategory", map, new TypeReference<Boolean>() {
                });
            }
        };
        task.setOnSucceeded(workerStateEvent -> {
            System.out.println("setOnSucceeded");
            if (!task.getValue().booleanValue()) return;
            String successMessage = "Категория успешно добавлена.";
            showSuccessNotification(successMessage);
            System.out.println("addCategory Task cool");
        });
        task.setOnFailed(workerStateEvent -> {
            System.out.println(task.getException().getMessage());
        });
        new Thread(task).start();
    }

    public void addHairdressingServices() {
        if (serviceNameTextField.equals("") || descriptionTextField.equals("") || durationTextField.equals("") || priceSlider.equals("") || categoryComboBox.getValue() == null) {
            // Если хотя бы одно поле ввода пустое или категория не выбрана, выводим сообщение об ошибке
            System.out.println("Заполните все поля, включая категорию");
            String errorMessage = "Заполните все поля, включая категорию!";
            showSuccessNotification(errorMessage);

            return;
        }
        System.out.println("addHairdressingServices");
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                System.out.println("addHairdressingServices 2222");
                Map<String, String> map = new HashMap<>();
                map.put("serviceNameTextField", serviceNameTextField.getText());
                map.put("description", descriptionTextField.getText());
                map.put("duration", durationTextField.getText());
                map.put("price", String.valueOf(priceSlider.getValue()));
                String selectedCategoryName = categoryComboBox.getValue();

                if (selectedCategoryName != null) {
                    System.out.println("addHairdressingServices 4444");

                    // Ищем объект категории по названию в листе categoryList
                    Category selectedCategory = null;
                    categoryList=setCategoriesList();
                    for (Category category : categoryList) {
                        if (category.getValue().equals(selectedCategoryName)) {
                            selectedCategory = category;
                            break;
                        }
                    }
                    System.out.println(selectedCategory + " selectedCategory");
                    if (selectedCategory != null) {
                        System.out.println(selectedCategory + " selectedCategory 222222");

                        try {
                            JSONObject categoryJson = new JSONObject();
                            categoryJson.put("id", selectedCategory.getId());
                            categoryJson.put("value", selectedCategory.getValue());
                            String categoryJsonString = categoryJson.toJSONString();
                            System.out.println(categoryJsonString + " categoryJson");

                            map.put("category", categoryJsonString);

                            System.out.println("Выбранная категория: " + selectedCategory.getValue());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Категория не найдена
                        String successMessage = "Выбранная категория не найдена";
                        showSuccessNotification(successMessage);
                    }
                } else {
                    // Категория не выбрана
                    String successMessage = "Категория не выбрана";
                    showSuccessNotification(successMessage);
                }
                System.out.println(map + " map");
                return service.requestGet("http://localhost:8080/addHairdressingServices", map, new TypeReference<Boolean>() {
                });
            }
        };
        new Thread(task).start();
    }


    private List<Category> categoryList = new ArrayList<>();

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

    public void populateComboBox(List<Category> categoryList) {
        // Очищаем существующие элементы в ComboBox
        categoryComboBox.getItems().clear();

        // Установка элементов списка в ComboBox
        Platform.runLater(() -> categoryComboBox.getItems().addAll(categoryList.stream()
                .map(Category::getValue)
                .collect(Collectors.toList())));
    }

    public void loadCategories() {
        Task<List<Category>> task = new Task<List<Category>>() {
            @Override
            protected List<Category> call() {
                return setCategoriesList();
            }
        };

        task.setOnSucceeded(event -> {
            List<Category> categoryList = task.getValue();
            populateComboBox(categoryList);
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            if (exception != null) {
                exception.printStackTrace();
            }
        });

        // Запускаем задачу в новом потоке
        new Thread(task).start();
    }

    private void showSuccessNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Уведомление");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/demo/startMenu.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }


    public void updateTable(ActionEvent actionEvent) throws ExecutionException {
        populateTable(setHairdressingServices());
    }
}
