package com.example.fabrichmetod;

import com.example.fabrichmetod.AbstrakClass;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    // FXML элементы
    @FXML private Canvas drawingCanvas;
    @FXML private ColorPicker colorPicker;
    @FXML private CheckBox filledCheckbox;
    @FXML private ComboBox<FactoryAbstrak.ShapeType> shapeComboBox;
    @FXML private Spinner<Integer> sidesSpinner;
    @FXML private ToggleGroup modeToggleGroup;
    @FXML private RadioButton drawModeRadio;
    @FXML private RadioButton moveModeRadio;
    @FXML private Button saveButton;
    @FXML private Button loadButton;
    @FXML private Button clearButton;
    @FXML private Label statusLabel;

    // Состояние приложения
    private List<AbstrakClass> shapes = new ArrayList<>();
    private AbstrakClass previewShape = null;
    private AbstrakClass selectedShape = null;
    private boolean isDrawingMode = true;

    // Координаты для рисования
    private double startX, startY;
    private double offsetX, offsetY;

    // Константы
    private static final String SAVE_FILENAME = "shapes.json";
    private FactoryAbstrak FactoryAbstrak;

    @FXML
    public void initialize() {
        System.out.println("Контроллер инициализирован");
        setupUIComponents();
        setupEventHandlers();
        setupCanvasHandlers();
        updateStatus();
    }

    private void setupUIComponents() {
        // Настройка выпадающего списка типов фигур
        shapeComboBox.getItems().addAll(FactoryAbstrak.ShapeType.values());
        shapeComboBox.setValue(FactoryAbstrak.ShapeType.RECTANGLE);

        // Обработчик изменения типа фигуры
        shapeComboBox.setOnAction(e -> {
            FactoryAbstrak.ShapeType selected = shapeComboBox.getValue();
            boolean isPolygon = selected == FactoryAbstrak.ShapeType.POLYGON;
            sidesSpinner.setVisible(isPolygon);
            updateStatus();
        });

        // Настройка спиннера для количества сторон
        sidesSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 12, 5)
        );
        sidesSpinner.setVisible(false);

        // Цвет по умолчанию
        colorPicker.setValue(Color.BLUE);

        // Режим по умолчанию - рисование
        drawModeRadio.setSelected(true);

        // Настройка стилей кнопок
        setupButtonStyles();
    }

    private void setupButtonStyles() {
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        loadButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        clearButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    private void setupEventHandlers() {
        // Обработчики кнопок
        saveButton.setOnAction(e -> saveShapesToFile());
        loadButton.setOnAction(e -> loadShapesFromFile());
        clearButton.setOnAction(e -> clearAllShapes());

        // Обработчик изменения режима
        modeToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                isDrawingMode = newToggle == drawModeRadio;
                updateStatus();
            }
        });

        // Обработчик изменения цвета
        colorPicker.setOnAction(e -> updateStatus());

        // Обработчик изменения заливки
        filledCheckbox.setOnAction(e -> updateStatus());
    }

    private void setupCanvasHandlers() {
        drawingCanvas.setOnMousePressed(this::handleCanvasMousePressed);
        drawingCanvas.setOnMouseDragged(this::handleCanvasMouseDragged);
        drawingCanvas.setOnMouseReleased(this::handleCanvasMouseReleased);
    }

    // ========== ОБРАБОТЧИКИ МЫШИ ==========

    private void handleCanvasMousePressed(MouseEvent event) {
        startX = event.getX();
        startY = event.getY();

        if (isDrawingMode) {
            // Начинаем рисовать новую фигуру
            previewShape = FactoryAbstrak.createPreviewShape(
                    shapeComboBox.getValue(),
                    startX, startY,
                    colorPicker.getValue(),
                    filledCheckbox.isSelected(),
                    sidesSpinner.getValue()
            );
            System.out.println("Начало рисования фигуры в (" + startX + ", " + startY + ")");
        } else {
            // Пытаемся выбрать фигуру для перемещения
            selectedShape = findShapeAt(startX, startY);
            if (selectedShape != null) {
                offsetX = startX - selectedShape.getStartX();
                offsetY = startY - selectedShape.getStartY();
                System.out.println("Выбрана фигура для перемещения: " + selectedShape.getType());
            }
        }
    }

    private void handleCanvasMouseDragged(MouseEvent event) {
        double currentX = event.getX();
        double currentY = event.getY();

        if (isDrawingMode && previewShape != null) {
            // Рисуем предпросмотр фигуры
            clearCanvas();
            redrawAllShapes();
            previewShape.drawPreview(drawingCanvas.getGraphicsContext2D(), currentX, currentY);
        } else if (!isDrawingMode && selectedShape != null) {
            // Перемещаем выбранную фигуру
            double newX = currentX - offsetX;
            double newY = currentY - offsetY;

            // Вычисляем смещение
            double deltaX = newX - selectedShape.getStartX();
            double deltaY = newY - selectedShape.getStartY();

            // Обновляем позицию фигуры
            selectedShape.setStartX(newX);
            selectedShape.setStartY(newY);

            // Особый случай для линии (нужно перемещать и конечную точку)
            if (selectedShape instanceof Line) {
                Line line = (Line) selectedShape;
                line.setEndX(line.getEndX() + deltaX);
                line.setEndY(line.getEndY() + deltaY);
            }

            clearCanvas();
            redrawAllShapes();
            startX = currentX;
            startY = currentY;
        }
    }

    private void handleCanvasMouseReleased(MouseEvent event) {
        double endX = event.getX();
        double endY = event.getY();

        if (isDrawingMode && previewShape != null) {
            // Проверяем минимальный размер фигуры
            double minDistance = 5.0;
            double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));

            if (distance >= minDistance) {
                // Создаем окончательную фигуру
                AbstrakClass finalShape = FactoryAbstrak.createFinalShape(
                        shapeComboBox.getValue(),
                        startX, startY,
                        endX, endY,
                        colorPicker.getValue(),
                        filledCheckbox.isSelected(),
                        sidesSpinner.getValue()
                );

                shapes.add(finalShape);
                System.out.println("Создана новая фигура: " + finalShape.getType() +
                        " (всего фигур: " + shapes.size() + ")");
            } else {
                System.out.println("Фигура слишком маленькая, не создана");
            }

            previewShape = null;
            clearCanvas();
            redrawAllShapes();
        } else {
            selectedShape = null;
        }

        updateStatus();
    }

    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    private AbstrakClass findShapeAt(double x, double y) {
        // Ищем с конца (последние нарисованные сверху)
        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (shapes.get(i).contains(x, y)) {
                return shapes.get(i);
            }
        }
        return null;
    }

    private void clearCanvas() {
        drawingCanvas.getGraphicsContext2D().clearRect(
                0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight()
        );
    }

    private void redrawAllShapes() {
        for (AbstrakClass shape : shapes) {
            shape.draw(drawingCanvas.getGraphicsContext2D());
        }
    }

    private void updateStatus() {
        String modeText = isDrawingMode ? "Рисование" : "Перемещение";
        String shapeText = shapeComboBox.getValue().toString();
        String colorText = colorPicker.getValue().toString();
        String filledText = filledCheckbox.isSelected() ? "с заливкой" : "без заливки";

        String status = String.format("Режим: %s | Фигура: %s | Цвет: %s | %s | Всего фигур: %d",
                modeText, shapeText, colorText, filledText, shapes.size());

        if (statusLabel != null) {
            statusLabel.setText(status);
        }
    }

    // ========== МЕТОДЫ ДЛЯ КНОПОК ==========

    private void saveShapesToFile() {
        try {
            JsonSerializer.saveShapesToFile(shapes, SAVE_FILENAME);
            showAlert("Успех",
                    "Фигуры успешно сохранены в файл: " + SAVE_FILENAME + "\n" +
                            "Сохранено фигур: " + shapes.size(),
                    Alert.AlertType.INFORMATION);
            System.out.println("Фигуры сохранены в " + SAVE_FILENAME);
        } catch (IOException e) {
            showAlert("Ошибка сохранения",
                    "Не удалось сохранить файл:\n" + e.getMessage(),
                    Alert.AlertType.ERROR);
            System.err.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private void loadShapesFromFile() {
        try {
            List<AbstrakClass> loadedShapes = JsonSerializer.loadShapesFromFile(SAVE_FILENAME);
            shapes = loadedShapes;
            clearCanvas();
            redrawAllShapes();

            showAlert("Успех",
                    "Фигуры успешно загружены из файла: " + SAVE_FILENAME + "\n" +
                            "Загружено фигур: " + shapes.size(),
                    Alert.AlertType.INFORMATION);
            System.out.println("Фигуры загружены из " + SAVE_FILENAME);
            updateStatus();
        } catch (IOException e) {
            showAlert("Ошибка загрузки",
                    "Не удалось загрузить файл:\n" + e.getMessage() + "\n" +
                            "Файл может не существовать или быть поврежден.",
                    Alert.AlertType.ERROR);
            System.err.println("Ошибка при загрузке: " + e.getMessage());
        }
    }

    private void clearAllShapes() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Подтверждение очистки");
        confirmation.setHeaderText("Очистить холст?");
        confirmation.setContentText("Вы уверены, что хотите удалить все фигуры?\nЭто действие нельзя отменить.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                shapes.clear();
                clearCanvas();
                System.out.println("Холст очищен");
                updateStatus();

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Холст очищен");
                info.setHeaderText(null);
                info.setContentText("Все фигуры удалены с холста.");
                info.showAndWait();
            }
        });
    }

    // ========== ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ==========

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Для отладки - выводит информацию о всех фигурах
     */
    public void printShapesInfo() {
        System.out.println("=== Информация о фигурах ===");
        System.out.println("Всего фигур: " + shapes.size());
        for (int i = 0; i < shapes.size(); i++) {
            AbstrakClass shape = shapes.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, shape.getType(), shape);
        }
        System.out.println("============================");
    }

    // Геттеры для тестирования
    public List<AbstrakClass> getShapes() {
        return new ArrayList<>(shapes);
    }

    public boolean isDrawingMode() {
        return isDrawingMode;
    }

    public int getShapesCount() {
        return shapes.size();
    }
}