package com.example.fabrichmetod;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

// КЛАСС-КОНТРОЛЛЕР для управления графическим интерфейсом
public class HelloController {
    // СВЯЗЫВАЕМ FXML ЭЛЕМЕНТЫ С КОДОМ
    @FXML
    private TextField sidesField;    // поле для ввода числа сторон

    @FXML
    private Label infoLabel;         // метка для вывода информации о фигуре

    @FXML
    private Canvas canvas;           // холст для рисования фигур

    private GraphicsContext gc;      // графический контекст для рисования


    // Метод инициализации - вызывается после загрузки FXML
    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        clearCanvas();
    }

    // ОБРАБОТЧИК СОБЫТИЙ для кнопки
    @FXML
    protected void drawShape() {
        String inputText = sidesField.getText();

        // ПРОВЕРКА ВВОДА
        if (!checkWithRegExp(inputText)) {
            showAlert("Введено не число или число не из диапазона от 0 до 5!");
            return;
        }

        int numberOfSides = Integer.parseInt(inputText);

        // ПРОВЕРКА ДИАПАЗОНА (0-5)
        if (numberOfSides < 0 || numberOfSides > 5) {
            showAlert("Число должно быть от 0 до 5!");
            return;
        }

        // ИСПОЛЬЗОВАНИЕ ФАБРИЧНОГО МЕТОДА
        FactoryAbstrak shapeFactory = new FactoryAbstrak();
        AbstrakClass shape = shapeFactory.createShape(numberOfSides);

        // Очистка холста и рисование фигуры
        clearCanvas();
        shape.draw(gc);

        // Вывод информации о фигуре
        infoLabel.setText(shape.descriptor());
    }

    // МЕТОД ПРОВЕРКИ РЕГУЛЯРНЫМ ВЫРАЖЕНИЕМ
    private boolean checkWithRegExp(String text) {
        // Проверяем, что строка состоит только из цифр
        return text != null && text.matches("\\d+");
    }

    // МЕТОД ДЛЯ ПОКАЗА ПРЕДУПРЕЖДЕНИЙ
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // МЕТОД ДЛЯ ОЧИСТКИ ХОЛСТА
    private void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // Заливаем белым фоном
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}