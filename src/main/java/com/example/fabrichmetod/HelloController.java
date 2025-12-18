package com.example.fabrichmetod;

import com.example.fabrichmetod.AbstrakClass;
import com.example.fabrichmetod.FactoryAbstrak;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.JsonSerializer;
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
    @FXML private RadioButton drawModeRadio;
    @FXML private RadioButton moveModeRadio;
    @FXML private Button saveButton;
    @FXML private Button loadButton;
    @FXML private Button clearButton;
    @FXML private Label statusLabel;
    @FXML private Label shapesCountLabel;
    @FXML private Button savedFilesButton;

    // Состояние приложения
    private List<AbstrakClass> shapes = new ArrayList<>();
    private AbstrakClass previewShape = null;
    private AbstrakClass selectedShape = null;
    private boolean isDrawingMode = true;

    // Координаты для рисования
    private double startX, startY;
    private double offsetX, offsetY;

    @FXML
    public void initialize() {
        System.out.println("🚀 Контроллер инициализирован");
        setupUIComponents();
        setupEventHandlers();
        setupCanvasHandlers();
        updateStatus();
        savedFilesButton.setOnAction(e -> showSavedFiles());
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

        // Создаем ToggleGroup программно
        ToggleGroup modeToggleGroup = new ToggleGroup();
        drawModeRadio.setToggleGroup(modeToggleGroup);
        moveModeRadio.setToggleGroup(modeToggleGroup);

        // Обработчик изменения режима
        modeToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                isDrawingMode = newToggle == drawModeRadio;
                updateStatus();
            }
        });
    }

    private void setupEventHandlers() {
        // Обработчики кнопок
        saveButton.setOnAction(e -> saveShapesToFile());
        loadButton.setOnAction(e -> loadShapesFromFile());
        clearButton.setOnAction(e -> clearAllShapes());
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
            System.out.println("🖱️ Начало рисования фигуры в (" + startX + ", " + startY + ")");
        } else {
            // Пытаемся выбрать фигуру для перемещения
            selectedShape = findShapeAt(startX, startY);
            if (selectedShape != null) {
                offsetX = startX - selectedShape.getStartX();
                offsetY = startY - selectedShape.getStartY();
                System.out.println("↔️ Выбрана фигура для перемещения: " + selectedShape.getType());
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
            selectedShape.setStartX(currentX - offsetX);
            selectedShape.setStartY(currentY - offsetY);

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
                System.out.println("✅ Создана новая фигура: " + finalShape.getType() +
                        " (всего фигур: " + shapes.size() + ")");
            } else {
                System.out.println("⚠️ Фигура слишком маленькая, не создана");
            }

            previewShape = null;
            clearCanvas();
            redrawAllShapes();
            updateStatus();
        } else {
            selectedShape = null;
        }
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

        if (shapesCountLabel != null) {
            shapesCountLabel.setText("Всего фигур: " + shapes.size());
        }
    }

    // ========== МЕТОДЫ ДЛЯ КНОПОК ==========

    private void saveShapesToFile() {
        try {
            // Сохраняем в файл по умолчанию
            JsonSerializer.saveToDefaultFile(shapes);

            // ИЛИ можно использовать диалог (раскомментировать):
            // JsonSerializer.saveWithDialog(drawingCanvas.getScene().getWindow(), shapes);

            showAlert("✅ Успех",
                    "Фигуры сохранены в файл 'shapes.json'!\n" +
                            "Сохранено фигур: " + shapes.size() + "\n" +
                            "Файл находится в папке с проектом.",
                    Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("❌ Ошибка сохранения",
                    "Не удалось сохранить файл:\n" + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private void loadShapesFromFile() {
        try {
            // Загружаем из файла по умолчанию
            List<AbstrakClass> loadedShapes = JsonSerializer.loadFromDefaultFile();

            // ИЛИ можно использовать диалог (раскомментировать):
            // List<AbstrakClass> loadedShapes = JsonSerializer.loadWithDialog(drawingCanvas.getScene().getWindow());

            if (!loadedShapes.isEmpty()) {
                shapes = loadedShapes;
                clearCanvas();
                redrawAllShapes();

                showAlert("✅ Успех",
                        "Фигуры загружены из файла 'shapes.json'!\n" +
                                "Загружено фигур: " + shapes.size(),
                        Alert.AlertType.INFORMATION);
                updateStatus();
            } else {
                showAlert("ℹ️ Информация",
                        "Файл 'shapes.json' не найден или пуст.\n" +
                                "Сначала сохраните фигуры.",
                        Alert.AlertType.INFORMATION);
            }
        } catch (IOException e) {
            showAlert("❌ Ошибка загрузки",
                    "Не удалось загрузить файл:\n" + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * НОВЫЙ МЕТОД: Показать список всех сохранений
     */
    private void showSavedFiles() {
        List<String> savedFiles = JsonSerializer.getSavedFiles();

        if (savedFiles.isEmpty()) {
            showAlert("📁 Сохранения",
                    "Нет сохраненных файлов.\n" +
                            "Сначала сохраните фигуры кнопкой 'Сохранить в JSON'.",
                    Alert.AlertType.INFORMATION);
        } else {
            StringBuilder filesList = new StringBuilder("Сохраненные файлы:\n\n");
            for (String file : savedFiles) {
                filesList.append("• ").append(file).append("\n");
            }

            showAlert("📁 Сохранения",
                    filesList.toString() +
                            "\nЧтобы загрузить, выберите 'shapes.json'",
                    Alert.AlertType.INFORMATION);
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
                System.out.println("🧹 Холст очищен");
                updateStatus();

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Холст очищен");
                info.setHeaderText(null);
                info.setContentText("Все фигуры удалены с холста.");
                info.showAndWait();
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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