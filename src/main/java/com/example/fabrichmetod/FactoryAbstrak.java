package com.example.fabrichmetod;

import com.example.fabrichmetod.AbstrakClass;
import javafx.scene.paint.Color;
import model.*; // Импорт всех классов из папки model
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
                double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
                return new Circle(startX, startY, radius, color, filled);

            case RECTANGLE:
                double width = endX - startX;
                double height = endY - startY;
                return new Rectangle(startX, startY, width, height, color, filled);

            case TRIANGLE:
                double triangleSize = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
                return new Triangle(startX, startY, triangleSize * 2, color, filled);

            case POLYGON:
                int polygonSides = (sides != null && sides >= 3) ? sides : 5;
                double polygonRadius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
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
}