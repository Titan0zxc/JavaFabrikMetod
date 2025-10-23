package com.example.fabrichmetod;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Загрузчик
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500); // Увеличил размер для холста
        stage.setTitle("Фабричный метод - Рисование фигур");
        stage.setScene(scene);
        stage.show();
    }
    
    // Запуск
    public static void main(String[] args) {
        launch();
    }
}