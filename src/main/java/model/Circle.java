import com.example.fabrichmetod.AbstrakClass;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class Circle extends AbstrakClass {
    private double radius;

    public Circle(double centerX, double centerY, double radius, Color color, boolean filled) {
        super(centerX, centerY, color, filled);
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setFill(filled ? color : Color.TRANSPARENT);

        // Рисуем круг с центром в (startX, startY)
        if (filled) {
            gc.fillOval(startX - radius, startY - radius, radius * 2, radius * 2);
        }
        gc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
    }

    @Override
    public void drawPreview(GraphicsContext gc, double currentX, double currentY) {
        double previewRadius = calculateDistance(startX, startY, currentX, currentY);
        Color previewColor = color.deriveColor(0, 1, 1, 0.5); // Полупрозрачный

        gc.setStroke(previewColor);
        gc.setFill(filled ? previewColor : Color.TRANSPARENT);
        gc.setLineDashes(5); // Пунктир для предпросмотра

        if (filled) {
            gc.fillOval(startX - previewRadius, startY - previewRadius,
                    previewRadius * 2, previewRadius * 2);
        }
        gc.strokeOval(startX - previewRadius, startY - previewRadius,
                previewRadius * 2, previewRadius * 2);
        gc.setLineDashes(); // Сброс пунктира
    }

    @Override
    public boolean contains(double x, double y) {
        // Проверяем, находится ли точка внутри круга
        double distance = calculateDistance(startX, startY, x, y);
        return distance <= radius;
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

    // Геттеры и сеттеры
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    // Для отладки
    @Override
    public String toString() {
        return String.format("Circle(center=(%.1f, %.1f), radius=%.1f, color=%s, filled=%s)",
                startX, startY, radius, color, filled);
    }
}