package com.example.fabrichmetod;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Загружаем FXML файл (нужно создать hello-view.fxml)
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

        // Настройка окна
        stage.setTitle("Рисовалка фигур - Фабричный метод + Абстрактный класс");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}