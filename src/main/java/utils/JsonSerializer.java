package utils;


import com.example.fabrichmetod.AbstrakClass;
import com.example.fabrichmetod.FactoryAbstrak;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.stage.FileChooser;
import javafx.stage.Window;
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

    /**
     * Сохраняет фигуры в выбранный файл через диалог
     */
    public static void saveWithDialog(Window window, List<AbstrakClass> shapes) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить фигуры");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON файлы", "*.json"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );
        fileChooser.setInitialFileName("мои_фигуры.json");

        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            saveShapesToFile(shapes, file.getAbsolutePath());
        }
    }

    /**
     * Загружает фигуры из выбранного файла через диалог
     */
    public static List<AbstrakClass> loadWithDialog(Window window) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить фигуры");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON файлы", "*.json"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );

        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            return loadShapesFromFile(file.getAbsolutePath());
        }
        return new ArrayList<>();
    }

    /**
     * Сохраняет в файл по умолчанию (shapes.json)
     */
    public static void saveToDefaultFile(List<AbstrakClass> shapes) throws IOException {
        saveShapesToFile(shapes, "shapes.json");
    }

    /**
     * Загружает из файла по умолчанию (shapes.json)
     */
    public static List<AbstrakClass> loadFromDefaultFile() throws IOException {
        return loadShapesFromFile("shapes.json");
    }

    /**
     * Сохраняет список фигур в указанный файл
     */
    private static void saveShapesToFile(List<AbstrakClass> shapes, String filename) throws IOException {
        if (shapes == null || shapes.isEmpty()) {
            System.out.println("⚠️ Нет фигур для сохранения");
            return;
        }

        List<Map<String, Object>> shapeMaps = new ArrayList<>();
        for (AbstrakClass shape : shapes) {
            shapeMaps.add(shape.toMap());
        }

        String json = gson.toJson(shapeMaps);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(json);
            System.out.println("✅ Сохранено " + shapes.size() + " фигур в: " + filename);
        }
    }

    /**
     * Загружает список фигур из указанного файла
     */
    private static List<AbstrakClass> loadShapesFromFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("⚠️ Файл не найден: " + filename);
            return new ArrayList<>();
        }

        String json = new String(Files.readAllBytes(Paths.get(filename)));

        if (json.trim().isEmpty()) {
            return new ArrayList<>();
        }

        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> shapeMaps = gson.fromJson(json, listType);

        List<AbstrakClass> shapes = new ArrayList<>();
        if (shapeMaps != null) {
            for (Map<String, Object> map : shapeMaps) {
                try {
                    AbstrakClass shape = FactoryAbstrak.createFromMap(map);
                    shapes.add(shape);
                } catch (Exception e) {
                    System.err.println("❌ Ошибка при загрузке фигуры: " + e.getMessage());
                }
            }
        }

        System.out.println("✅ Загружено " + shapes.size() + " фигур из: " + filename);
        return shapes;
    }

    /**
     * Получает список всех сохраненных файлов в директории проекта
     */
    public static List<String> getSavedFiles() {
        List<String> files = new ArrayList<>();
        File dir = new File(".");

        File[] jsonFiles = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (jsonFiles != null) {
            for (File file : jsonFiles) {
                files.add(file.getName());
            }
        }

        return files;
    }
}