package com.example.demo;

import com.example.demo.model.Category;
import com.example.demo.model.HairdressingServiceInfo;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceDetailsDialog<T> extends Dialog<T> {
    private HairdressingServiceInfo serviceInfo;
    private String username;

    public ServiceDetailsDialog(){};
    public ServiceDetailsDialog(HairdressingServiceInfo serviceInfo, String username) {
        this.serviceInfo = serviceInfo;
        this.username = username;
        String greetingLabel = "Привет, " + username + "! Тут можешь записаться на услугу `"+serviceInfo.getName()+"` в нашей парикмахерской";

        // Настройка внешнего вида диалогового окна
        setTitle("Детали услуги");
        setHeaderText(String.valueOf(greetingLabel));

        // Создание компонентов для отображения информации о сервисе
        Label descriptionLabel = new Label("Описание: " + serviceInfo.getDescription());
        Label categoryLabel = new Label("Категория: " + serviceInfo.getCategory().getValue());
        Label durationLabel = new Label("Длительность: " + (serviceInfo.getDuration())+"ч.");
        Label priceLabel = new Label("Цена: " + format(serviceInfo.getPrice())+"$");
        // Создание компонента для ввода комментария
        TextArea commentTextArea = new TextArea();
        commentTextArea.setPromptText("Введите комментарий");

        // Создание контейнера для компонентов
        VBox content = new VBox(descriptionLabel, categoryLabel, durationLabel, priceLabel, commentTextArea);
        content.setSpacing(10);
        content.setPadding(new Insets(10));
        getDialogPane().setContent(content);

        // Установка содержимого диалогового окна
        getDialogPane().setContent(content);


        // Настройка кнопок диалогового окна
        ButtonType bookButton = new ButtonType("Записаться", ButtonBar.ButtonData.OK_DONE);
        ButtonType closeButton = new ButtonType("Закрыть", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(bookButton, closeButton);

        // Закрытие диалогового окна при нажатии кнопки "Закрыть"
        setResultConverter(buttonType -> {
            if (buttonType == bookButton) {
                String userComment = commentTextArea.getText();
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("service", serviceInfo);
                resultMap.put("comment", userComment);

                return (T) resultMap; // Возвращает объект HairdressingServiceInfo
            }
            return null;
        });
    }

    private String format(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(price);
    }
}