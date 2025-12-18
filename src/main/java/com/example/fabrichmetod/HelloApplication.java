package com.example.fabrichmetod;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("🚀 Запуск приложения...");

        // Правильный путь с учетом пакета
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

        stage.setTitle("Рисовалка фигур - Фабричный метод + JSON");
        stage.setScene(scene);
        stage.show();

        System.out.println("✅ Приложение запущено!");
    }

    public static void main(String[] args) {
        launch();
    }
}