package model; // Важно: должен быть в пакете model

import com.example.fabrichmetod.AbstrakClass;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

// Ключевое изменение: extends AbstrakClass
public class Circle extends AbstrakClass {
    private double radius;

    // Конструктор теперь вызывает super()
    public Circle(double centerX, double centerY, double radius, Color color, boolean filled) {
        super(centerX, centerY, color, filled); // Вызов конструктора родителя
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setFill(filled ? color : Color.TRANSPARENT);

        if (filled) {
            gc.fillOval(startX - radius, startY - radius, radius * 2, radius * 2);
        }
        gc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
    }

    @Override
    public void drawPreview(GraphicsContext gc, double currentX, double currentY) {
        double previewRadius = calculateDistance(startX, startY, currentX, currentY);
        Color previewColor = color.deriveColor(0, 1, 1, 0.5);

        gc.setStroke(previewColor);
        gc.setFill(filled ? previewColor : Color.TRANSPARENT);
        gc.setLineDashes(5);

        if (filled) {
            gc.fillOval(startX - previewRadius, startY - previewRadius,
                    previewRadius * 2, previewRadius * 2);
        }
        gc.strokeOval(startX - previewRadius, startY - previewRadius,
                previewRadius * 2, previewRadius * 2);
        gc.setLineDashes();
    }

    @Override
    public boolean contains(double x, double y) {
        double dist = calculateDistance(startX, startY, x, y);
        return dist <= radius;
    }

    @Override
    public String getType() {
        return "CIRCLE";
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        map.put("centerX", startX);
        map.put("centerY", startY);
        map.put("radius", radius);
        map.put("color", color.toString());
        map.put("filled", filled);
        return map;
    }

    // Геттер и сеттер
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}