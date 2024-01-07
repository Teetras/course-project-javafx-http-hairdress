package com.example.demo.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.example.demo.GenericService;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button authSingInButton;

    @FXML
    private TextField fio_field;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private TextField phone_field;

    @FXML
    private Button registerDoneButton;

    GenericService service = new GenericService.Base();
        @FXML
    void initialize() {

            authSingInButton.setOnAction(actionEvent -> {
                authSingInButton.getScene().getWindow().hide();
                FXMLLoader loader= new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/demo/hello-view.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Parent root =loader.getRoot();
                Stage stage=new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();
            });
            registerDoneButton.setOnAction(actionEvent -> {
                String login = login_field.getText();
                String password = password_field.getText();
                String name = fio_field.getText();
                String phone = phone_field.getText();

                if (login.isEmpty() || password.isEmpty()||name.isEmpty()||phone.isEmpty()) {
                    showAlert("Ошибка", "Поля логина и пароля должны быть заполнены.");
                    return;
                }
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    Map<String,String> map= new HashMap<>();
                    map.put("login",login_field.getText());
                    map.put("password",password_field.getText());
                    map.put("name",fio_field.getText());
                    map.put("phone",phone_field.getText());


                    return service.requestGet("http://localhost:8080/register", map, new TypeReference<Boolean>() {});
                }
            };

            task.setOnSucceeded(workerStateEvent -> {
                System.out.println("setOnSucceeded");
                if(!task.getValue().booleanValue()) return;
                authSingInButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/hello-view.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            });
         task.setOnFailed(workerStateEvent -> {
             System.out.println(task.getException().getMessage());
         });
         new Thread(task).start();
        });
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
