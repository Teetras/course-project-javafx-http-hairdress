package com.example.demo.controllers;
import com.example.demo.model.User;
import javafx.scene.control.*;
import org.controlsfx.control.MaskerPane;
import com.example.demo.GenericService;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloController {
    User user=null;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button authSingInButton;

    @FXML
    private Button loginSingUpButton;

    @FXML
    private TextField login_field;
    MaskerPane maskerPane = new MaskerPane();
    @FXML
    private PasswordField password_field;
    GenericService service = new GenericService.Base();
    @FXML
    void initialize(){


        loginSingUpButton.setOnAction(actionEvent -> {
            loginSingUpButton.getScene().getWindow().hide();
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/demo/register.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Parent root =loader.getRoot();
            Stage stage=new Stage();

            stage.setScene(new Scene(root));
            // Установка окна сцены неразделимым
            stage.setResizable(false);
            stage.showAndWait();
        });
    };



    private void loginUser(String loginText, String passText) {

    }

    public void click(ActionEvent actionEvent) {
        String login = login_field.getText();
        String password = password_field.getText();

        if (login.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Поля логина и пароля должны быть заполнены.");
            return;
        }
        progressIndicator.setVisible(true);
        System.out.println(login_field.getText());

        Task<User> task = new Task<User>() {
            @Override
            protected User call() throws Exception {
                Map<String, String> map = new HashMap<>();
                map.put("login", login_field.getText());
                map.put("password", password_field.getText());

                TypeReference<User> responseType = new TypeReference<User>() {};
                 user = service.requestGet("http://localhost:8080/login", map, responseType);
                System.out.println(user.getId()+"  id");
                return user;
            }
        };
        task.setOnRunning(workerStateEvent -> {
            progressIndicator.setVisible(true);
        });

        task.setOnSucceeded(workerStateEvent -> {
            progressIndicator.setVisible(false);
            System.out.println("setOnSucceeded");
            User user = task.getValue();
            if (user != null) {
                authSingInButton.getScene().getWindow().hide();
                FXMLLoader loader = new FXMLLoader();

                // определение типа окна, которое будет открыто
                String fxmlPath;
                if (user.getIsAdmin() == 1) {
                    fxmlPath = "/com/example/demo/startMenu.fxml";
                } else if (user.getIsWorker() == 1) {
                    fxmlPath = "/com/example/demo/startMenu.fxml";
                } else {
                    fxmlPath = "/com/example/demo/startMenuForUser.fxml";
                }

                loader.setLocation(getClass().getResource(fxmlPath));
                try {
                    loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(fxmlPath == "/com/example/demo/startMenuForUser.fxml"){
                    System.out.println(user.getId());
                    StartMenuForUserController controller =loader.getController();

                    try {
                        controller.initialize(user);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(fxmlPath == "/com/example/demo/startMenu.fxml"){
                    System.out.println(user.getId());
                    HomeController controller =loader.getController();

                    try {
                        controller.initialize(user);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            }
        });
        task.setOnFailed(workerStateEvent -> {
            System.out.println(task.getException().getMessage());
        });
        new Thread(task).start();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}