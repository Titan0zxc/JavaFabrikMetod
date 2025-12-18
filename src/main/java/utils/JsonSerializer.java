package utils;

import com.example.fabrichmetod.AbstrakClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import javax.sound.sampled.Line;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonSerializer {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final String DEFAULT_FILENAME = "shapes.json";
    private static FactoryAbstrak FactoryAbstrak;

    /**
     * Сохраняет список фигур в JSON файл
     */
    public static void saveShapesToFile(List<AbstrakClass> shapes, String filename) throws IOException {
        // Преобразуем фигуры в список Map для сериализации
        List<Map<String, Object>> shapeMaps = new ArrayList<>();
        for (AbstrakClass shape : shapes) {
            shapeMaps.add(shape.toMap());
        }

        // Сериализуем в JSON
        String json = gson.toJson(shapeMaps);

        // Записываем в файл
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(json);
            System.out.println("Сохранено " + shapes.size() + " фигур в файл: " + filename);
        }
    }

    /**
     * Сохраняет в файл по умолчанию (shapes.json)
     */
    public static void saveShapesToFile(List<AbstrakClass> shapes) throws IOException {
        saveShapesToFile(shapes, DEFAULT_FILENAME);
    }

    /**
     * Загружает список фигур из JSON файла
     */
    public static List<AbstrakClass> loadShapesFromFile(String filename) throws IOException {
        // Проверяем существование файла
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Файл " + filename + " не найден. Возвращаем пустой список.");
            return new ArrayList<>();
        }

        // Читаем JSON из файла
        String json = new String(Files.readAllBytes(Paths.get(filename)));

        // Десериализуем в список Map
        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> shapeMaps = gson.fromJson(json, listType);

        // Преобразуем Map обратно в фигуры
        List<AbstrakClass> shapes = new ArrayList<>();
        if (shapeMaps != null) {
            for (Map<String, Object> map : shapeMaps) {
                try {
                    AbstrakClass shape = FactoryAbstrak.createFromMap(map);
                    shapes.add(shape);
                } catch (Exception e) {
                    System.err.println("Ошибка при создании фигуры из JSON: " + e.getMessage());
                }
            }
        }

        System.out.println("Загружено " + shapes.size() + " фигур из файла: " + filename);
        return shapes;
    }

    /**
     * Загружает из файла по умолчанию (shapes.json)
     */
    public static List<AbstrakClass> loadShapesFromFile() throws IOException {
        return loadShapesFromFile(DEFAULT_FILENAME);
    }

    /**
     * Экспорт фигур в JSON строку (для буфера обмена или отладки)
     */
    public static String exportShapesToString(List<AbstrakClass> shapes) {
        List<Map<String, Object>> shapeMaps = new ArrayList<>();
        for (AbstrakClass shape : shapes) {
            shapeMaps.add(shape.toMap());
        }
        return gson.toJson(shapeMaps);
    }

    /**
     * Импорт фигур из JSON строки
     */
    public static List<AbstrakClass> importShapesFromString(String json) {
        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> shapeMaps = gson.fromJson(json, listType);

        List<AbstrakClass> shapes = new ArrayList<>();
        if (shapeMaps != null) {
            for (Map<String, Object> map : shapeMaps) {
                try {
                    AbstrakClass shape = FactoryAbstrak.createFromMap(map);
                    shapes.add(shape);
                } catch (Exception e) {
                    System.err.println("Ошибка при импорте фигуры: " + e.getMessage());
                }
            }
        }

        return shapes;
    }

    /**
     * Тестирование сериализации/десериализации
     */
    public static void testSerialization() {
        List<AbstrakClass> testShapes = new ArrayList<>();
        testShapes.add(new Line(10, 10, 100, 100, javafx.scene.paint.Color.RED));
        testShapes.add(new Circle(150, 150, 50, javafx.scene.paint.Color.BLUE, true));
        testShapes.add(new Rectangle(200, 200, 80, 60, javafx.scene.paint.Color.GREEN, false));

        try {
            // Сохраняем
            saveShapesToFile(testShapes, "test_shapes.json");
            System.out.println("Тестовые фигуры сохранены");

            // Загружаем
            List<AbstrakClass> loaded = loadShapesFromFile("test_shapes.json");
            System.out.println("Загружено фигур: " + loaded.size());

            // Выводим JSON
            String json = exportShapesToString(testShapes);
            System.out.println("JSON:\n" + json);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}