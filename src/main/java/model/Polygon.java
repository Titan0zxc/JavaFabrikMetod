import com.example.fabrichmetod.AbstrakClass;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class Polygon extends AbstrakClass {
    private int sides;        // Количество сторон
    private double radius;    // Радиус описанной окружности
    private double[] xPoints; // Координаты X вершин
    private double[] yPoints; // Координаты Y вершин

    public Polygon(double centerX, double centerY, int sides, double radius,
                   Color color, boolean filled) {
        super(centerX, centerY, color, filled);
        this.sides = Math.max(3, sides); // Минимум 3 стороны
        this.radius = radius;
        calculateVertices();
    }

    // Вычисляем координаты вершин правильного многоугольника
    private void calculateVertices() {
        xPoints = new double[sides];
        yPoints = new double[sides];

        for (int i = 0; i < sides; i++) {
            // Угол в радианах для каждой вершины
            double angle = 2 * Math.PI * i / sides;

            // Вычисляем координаты вершины
            xPoints[i] = startX + radius * Math.cos(angle);
            yPoints[i] = startY + radius * Math.sin(angle);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setFill(filled ? color : Color.TRANSPARENT);

        if (filled) {
            gc.fillPolygon(xPoints, yPoints, sides);
        }
        gc.strokePolygon(xPoints, yPoints, sides);
    }

    @Override
    public void drawPreview(GraphicsContext gc, double currentX, double currentY) {
        double previewRadius = calculateDistance(startX, startY, currentX, currentY);

        // Вычисляем вершины для предпросмотра
        double[] previewXPoints = new double[sides];
        double[] previewYPoints = new double[sides];

        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides;
            previewXPoints[i] = startX + previewRadius * Math.cos(angle);
            previewYPoints[i] = startY + previewRadius * Math.sin(angle);
        }

        Color previewColor = color.deriveColor(0, 1, 1, 0.5);
        gc.setStroke(previewColor);
        gc.setFill(filled ? previewColor : Color.TRANSPARENT);
        gc.setLineDashes(5);

        if (filled) {
            gc.fillPolygon(previewXPoints, previewYPoints, sides);
        }
        gc.strokePolygon(previewXPoints, previewYPoints, sides);
        gc.setLineDashes();
    }

    @Override
    public boolean contains(double x, double y) {
        // Проверяем, находится ли точка внутри многоугольника (алгоритм ray casting)
        return pointInPolygon(x, y);
    }

    // Алгоритм ray casting для проверки точки внутри многоугольника
    private boolean pointInPolygon(double x, double y) {
        boolean inside = false;

        for (int i = 0, j = sides - 1; i < sides; j = i++) {
            if (((yPoints[i] > y) != (yPoints[j] > y)) &&
                    (x < (xPoints[j] - xPoints[i]) * (y - yPoints[i]) /
                            (yPoints[j] - yPoints[i]) + xPoints[i])) {
                inside = !inside;
            }
        }

        return inside;
    }

    @Override
    public String getType() {
        return "POLYGON";
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        map.put("centerX", startX);
        map.put("centerY", startY);
        map.put("sides", sides);
        map.put("radius", radius);
        map.put("color", color.toString());
        map.put("filled", filled);
        return map;
    }

    // Геттеры и сеттеры
    public int getSides() {
        return sides;
    }

    public void setSides(int sides) {
        this.sides = Math.max(3, sides);
        calculateVertices(); // Пересчитываем вершины
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        calculateVertices(); // Пересчитываем вершины
    }

    public double[] getXPoints() {
        return xPoints.clone();
    }

    public double[] getYPoints() {
        return yPoints.clone();
    }

    // Вспомогательные методы
    public double getArea() {
        // Площадь правильного n-угольника
        return (sides * radius * radius * Math.sin(2 * Math.PI / sides)) / 2;
    }

    public double getPerimeter() {
        // Периметр правильного n-угольника
        return sides * 2 * radius * Math.sin(Math.PI / sides);
    }

    @Override
    public String toString() {
        return String.format("Polygon(center=(%.1f, %.1f), sides=%d, radius=%.1f, color=%s, filled=%s)",
                startX, startY, sides, radius, color, filled);
    }
}