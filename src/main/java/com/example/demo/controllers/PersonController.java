package com.example.demo.controllers;

import com.example.demo.GenericService;
import com.example.demo.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class PersonController {
    @FXML
    private TableView<Order> tableView;
    GenericService service = new GenericService.Base();

    @FXML
    private Label nameLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label phoneLabel;
    @FXML
    private Label skidka;
    @FXML
    private Label totalCostLabel;
    private User user;

    int orderCount;
    double  totalCost;
    @FXML
    void initialize(User userC) throws ExecutionException {
        user = userC;
        System.out.println(user.getLogin() + " lo");

        populateTable(setOrders()); // Вывод информации о персоне в JavaFX
        nameLabel.setText("ФИО: " +user.getPerson_id().getName());
        ageLabel.setText("Телефон: " + user.getPerson_id().getPhone());
        addressLabel.setText("Логин: " + user.getLogin());
        phoneLabel.setText("Количество заказов: " + orderCount);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedTotalCost = decimalFormat.format(totalCost);
        totalCostLabel.setText("Общая сумма заказов: " + formattedTotalCost);
        skidka.setText("Персональная скидка: 5%" );
    }

    @FXML
    private void openRatingDialog() {
        Order selectedOrder = tableView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            // Создание диалогового окна
            Dialog<Pair<Integer, String>> dialog = new Dialog<>();
            dialog.setTitle("Оценка и комментарий");

            // Создание кнопок "Оценить" и "Отмена"
            ButtonType rateButtonType = new ButtonType("Оценить", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(rateButtonType, ButtonType.CANCEL);

            // Создание контролов для рейтинга и комментария
            Slider ratingSlider = new Slider(0, 10, 0);
            ratingSlider.setShowTickLabels(true);
            ratingSlider.setShowTickMarks(true);
            ratingSlider.setMajorTickUnit(1);
            ratingSlider.setBlockIncrement(1);

            TextField commentTextField = new TextField();
            commentTextField.setPromptText("Напишите комментарий...");

            // Отображение информации о заказе
            Label orderInfoLabel = new Label("Информация о заказе:\n" +
                    "Номер заказа: " + selectedOrder.getId() + "\n" +
                    "Дата заказа: " + selectedOrder.getOrderDate() + "\n" +
                    "На Имя: " +user.getPerson_id().getName() + "\n" +
                    "Стоимость: " + selectedOrder.getHairdressingService().getPrice());

            // Размещение контролов в диалоговом окне
            VBox vbox = new VBox(10);
            vbox.getChildren().addAll(orderInfoLabel, new Label("Рейтинг:"), ratingSlider, new Label("Комментарий:"), commentTextField);
            dialog.getDialogPane().setContent(vbox);

            // Валидация введенных данных
            Node rateButton = dialog.getDialogPane().lookupButton(rateButtonType);
            rateButton.setDisable(true);
            ratingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                rateButton.setDisable(false);
            });

            // Установка результатов диалога при нажатии кнопки "Оценить"
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == rateButtonType) {
                    return new Pair<>((int) ratingSlider.getValue(), commentTextField.getText());
                }
                return null;
            });

            // Отображение диалогового окна и обработка результатов
            Optional<Pair<Integer, String>> result = dialog.showAndWait();

            result.ifPresent(ratingComment -> {
                int rating = ratingComment.getKey() + 1;
                String comment = ratingComment.getValue();
                System.out.println(rating);

                LocalDate localDate = LocalDate.now();
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(date);

                System.out.println("addReview");

                String url = "http://localhost:8080/addReview";
                Map<String, String> map = new HashMap<>();
                map.put("comment", comment);
                map.put("rating", String.valueOf(rating));
                map.put("date", dateString);
                map.put("idOrder", String.valueOf(selectedOrder.getId()));

                try {
                    System.out.println(map);
                    service.requestGet(url, map, new TypeReference<Boolean>(){});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
    }

    @FXML
    private void onTableItemDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // Двойной клик
            Order selectedOrder = tableView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                // Выполните необходимые действия с выбранным заказом
                // Например, откройте детали заказа или выполните другую логику
            }
        }
    }

    public void populateTable(List<Order> data) {
        // Очистка таблицы перед заполнением
        tableView.getItems().clear();
        String userId = user.getId();

        List<Order> filteredData = data.stream()
                .filter(order -> order.getUser().getId().equals(userId))
                .collect(Collectors.toList());
        System.out.println(filteredData + "  filteredData");
        orderCount = filteredData.size();
        System.out.println("Количество заказов: " + orderCount);

         totalCost = filteredData.stream()
                .mapToDouble(order -> order.getHairdressingService().getPrice())
                .sum();
        System.out.println("Общая стоимость заказов: " + totalCost);
        ObservableList<Order> tableData = FXCollections.observableArrayList(filteredData);

        TableColumn<Order, String> serviceNameColumn = (TableColumn<Order, String>) tableView.getColumns().get(0);
        TableColumn<Order, String> descriptionColumn = (TableColumn<Order, String>) tableView.getColumns().get(1);
        TableColumn<Order, String> serviceTypeColumn = (TableColumn<Order, String>) tableView.getColumns().get(2);
        TableColumn<Order, String> durationColumn = (TableColumn<Order, String>) tableView.getColumns().get(3);
        TableColumn<Order, String> costColumn = (TableColumn<Order, String>) tableView.getColumns().get(4);
        TableColumn<Order, LocalDate> dateColumn = (TableColumn<Order, LocalDate>) tableView.getColumns().get(5);

        serviceNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHairdressingService().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHairdressingService().getDescription()));
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHairdressingService().getDuration()));
        serviceTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHairdressingService().getCategory().getValue()));

        costColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getHairdressingService().getPrice())));
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOrderDate()));

        // Установка преобразователя значений для ячейки столбца даты
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        dateColumn.setCellFactory(column -> new TableCell<Order, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(dateFormatter));
                }
            }
        });

        tableView.setItems(tableData);
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

    public void onTableItemClick(MouseEvent mouseEvent) {
        openRatingDialog();
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.hide();
        FXMLLoader loader = new FXMLLoader();
        String fxmlPath = "/com/example/demo/startMenuForUser.fxml";

        loader.setLocation(getClass().getResource(fxmlPath));
        if (fxmlPath.equals("/com/example/demo/startMenuForUser.fxml")) {
            try {
                loader.load();
                StartMenuForUserController controller = loader.getController();
                System.out.println(user.getId());
                controller.initialize(user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void buy(ActionEvent actionEvent) {
        Dialog<Double> dialog = new Dialog<>();
        dialog.setTitle("Оплата заказа");

        // Создание кнопок "Оплатить" и "Отмена"
        ButtonType payButtonType = new ButtonType("Оплатить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(payButtonType, ButtonType.CANCEL);

        // Создание контролов для ввода суммы и вывода сдачи
        TextField totalCostTextField = new TextField();
        totalCostTextField.setPromptText("Введите сумму");

        Label changeLabel = new Label("Сдача: ");
        Label cost = new Label("Общая стоимость: "+totalCost);

        // Размещение контролов в диалоговом окне
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        vbox.getChildren().addAll( new Label("Общая стоимость:"),totalCostLabel,new Label("Ваша сумма:"), totalCostTextField,  changeLabel);
        dialog.getDialogPane().setContent(vbox);

        // Валидация введенных данных
        Node payButton = dialog.getDialogPane().lookupButton(payButtonType);
        payButton.setDisable(true);

        totalCostTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double totalCost = Double.parseDouble(newValue);
                payButton.setDisable(false);
                calculateChange( changeLabel,totalCost);
            } catch (NumberFormatException e) {
                payButton.setDisable(true);
                changeLabel.setText("Сдача: ");
            }
        });

        // Установка результатов диалога при нажатии кнопки "Оплатить"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == payButtonType) {
                try {
                    double totalCost = Double.parseDouble(totalCostTextField.getText());
                    return totalCost;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        // Отображение диалогового окна и обработка результатов
        Optional<Double> result = dialog.showAndWait();
        result.ifPresent(amount -> {
            // Выполнение оплаты
            System.out.println("Оплата выполнена. Сумма: " + amount);
        });
    }

    private void calculateChange( Label changeLabel, double text) {


        // Проверка, достаточно ли введенной суммы для оплаты
        if (text < totalCost) {
            changeLabel.setText("Сумма слишком мала");
        } else {
            // Вычисление сдачи
            double change = text - totalCost;
            changeLabel.setText("Сдача: " + change);
        }

    }
}

