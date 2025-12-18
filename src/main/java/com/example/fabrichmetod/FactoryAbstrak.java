import com.example.fabrichmetod.AbstrakClass;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import javax.sound.sampled.Line;
import java.util.Map;

public class FactoryAbstrak {
    // Enum для типов фигур
    public enum ShapeType {
        LINE, CIRCLE, RECTANGLE, TRIANGLE, POLYGON
    }

    /**
     * Создание фигуры для предпросмотра (во время рисования)
     */
    public static AbstrakClass createPreviewShape(ShapeType type, double startX, double startY,
                                                  Color color, boolean filled, Integer sides) {
        switch(type) {
            case LINE:
                return new Line(startX, startY, startX, startY, color);
            case CIRCLE:
                return new Circle(startX, startY, 0, color, filled);
            case RECTANGLE:
                return new Rectangle(startX, startY, 0, 0, color, filled);
            case TRIANGLE:
                return new Triangle(startX, startY, 0, color, filled);
            case POLYGON:
                int polygonSides = (sides != null && sides >= 3) ? sides : 5;
                return new Polygon(startX, startY, polygonSides, 0, color, filled);
            default:
                throw new IllegalArgumentException("Неизвестный тип фигуры: " + type);
        }
    }

    /**
     * Создание окончательной фигуры
     */
    public static AbstrakClass createFinalShape(ShapeType type, double startX, double startY,
                                                double endX, double endY, Color color,
                                                boolean filled, Integer sides) {
        switch(type) {
            case LINE:
                return new Line(startX, startY, endX, endY, color);

            case CIRCLE:
                double radius = calculateDistance(startX, startY, endX, endY);
                return new Circle(startX, startY, radius, color, filled);

            case RECTANGLE:
                double width = endX - startX;
                double height = endY - startY;
                return new Rectangle(startX, startY, width, height, color, filled);

            case TRIANGLE:
                double triangleSize = calculateDistance(startX, startY, endX, endY);
                return new Triangle(startX, startY, triangleSize * 2, color, filled);

            case POLYGON:
                int polygonSides = (sides != null && sides >= 3) ? sides : 5;
                double polygonRadius = calculateDistance(startX, startY, endX, endY);
                return new Polygon(startX, startY, polygonSides, polygonRadius, color, filled);

            default:
                throw new IllegalArgumentException("Неизвестный тип фигуры: " + type);
        }
    }

    /**
     * Создание фигуры из Map (для загрузки из JSON)
     */
    public static AbstrakClass createFromMap(Map<String, Object> map) {
        String type = (String) map.get("type");
        Color color = Color.valueOf((String) map.get("color"));
        boolean filled = map.containsKey("filled") ? (boolean) map.get("filled") : false;

        switch(type) {
            case "LINE":
                return new Line(
                        (double) map.get("startX"),
                        (double) map.get("startY"),
                        (double) map.get("endX"),
                        (double) map.get("endY"),
                        color
                );

            case "CIRCLE":
                return new Circle(
                        (double) map.get("centerX"),
                        (double) map.get("centerY"),
                        (double) map.get("radius"),
                        color,
                        filled
                );

            case "RECTANGLE":
                return new Rectangle(
                        (double) map.get("x"),
                        (double) map.get("y"),
                        (double) map.get("width"),
                        (double) map.get("height"),
                        color,
                        filled
                );

            case "TRIANGLE":
                return new Triangle(
                        (double) map.get("centerX"),
                        (double) map.get("centerY"),
                        (double) map.get("sideLength"),
                        color,
                        filled
                );

            case "POLYGON":
                int sides = ((Double) map.get("sides")).intValue();
                return new Polygon(
                        (double) map.get("centerX"),
                        (double) map.get("centerY"),
                        sides,
                        (double) map.get("radius"),
                        color,
                        filled
                );

            default:
                throw new IllegalArgumentException("Неизвестный тип фигуры в JSON: " + type);
        }
    }

    /**
     * Вспомогательный метод для вычисления расстояния
     */
    private static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Тестирование фабрики
     */
    public static void testFactory() {
        System.out.println("=== Тестирование фабрики фигур ===");

        // Создаем несколько фигур
        AbstrakClass line = createFinalShape(ShapeType.LINE, 10, 10, 100, 100, Color.RED, false, null);
        AbstrakClass circle = createFinalShape(ShapeType.CIRCLE, 150, 150, 200, 200, Color.BLUE, true, null);
        AbstrakClass rect = createFinalShape(ShapeType.RECTANGLE, 200, 200, 300, 250, Color.GREEN, false, null);
        AbstrakClass triangle = createFinalShape(ShapeType.TRIANGLE, 300, 300, 350, 350, Color.ORANGE, true, null);
        AbstrakClass polygon = createFinalShape(ShapeType.POLYGON, 400, 400, 450, 450, Color.PURPLE, true, 6);

        System.out.println("Создано фигур: 5");
        System.out.println("Линия: " + line.getType());
        System.out.println("Круг: " + circle.getType());
        System.out.println("Прямоугольник: " + rect.getType());
        System.out.println("Треугольник: " + triangle.getType());
        System.out.println("Многоугольник: " + polygon.getType());
    }
}