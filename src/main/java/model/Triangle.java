import com.example.fabrichmetod.AbstrakClass;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class Triangle extends AbstrakClass {
    private double sideLength;
    private double[] xPoints = new double[3];
    private double[] yPoints = new double[3];

    public Triangle(double centerX, double centerY, double sideLength,
                    Color color, boolean filled) {
        super(centerX, centerY, color, filled);
        this.sideLength = sideLength;
        calculateVertices();
    }

    // Вычисляем вершины равностороннего треугольника
    private void calculateVertices() {
        // Высота равностороннего треугольника
        double height = (Math.sqrt(3) / 2) * sideLength;

        // Вершина 1 (верхняя)
        xPoints[0] = startX;
        yPoints[0] = startY - (2.0/3.0) * height;

        // Вершина 2 (левая нижняя)
        xPoints[1] = startX - sideLength / 2;
        yPoints[1] = startY + height / 3;

        // Вершина 3 (правая нижняя)
        xPoints[2] = startX + sideLength / 2;
        yPoints[2] = startY + height / 3;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setFill(filled ? color : Color.TRANSPARENT);

        if (filled) {
            gc.fillPolygon(xPoints, yPoints, 3);
        }
        gc.strokePolygon(xPoints, yPoints, 3);
    }

    @Override
    public void drawPreview(GraphicsContext gc, double currentX, double currentY) {
        // Для предпросмотра вычисляем размер на основе расстояния от центра
        double previewSideLength = calculateDistance(startX, startY, currentX, currentY) * 2;
        double height = (Math.sqrt(3) / 2) * previewSideLength;

        double[] previewXPoints = new double[3];
        double[] previewYPoints = new double[3];

        previewXPoints[0] = startX;
        previewYPoints[0] = startY - (2.0/3.0) * height;

        previewXPoints[1] = startX - previewSideLength / 2;
        previewYPoints[1] = startY + height / 3;

        previewXPoints[2] = startX + previewSideLength / 2;
        previewYPoints[2] = startY + height / 3;

        Color previewColor = color.deriveColor(0, 1, 1, 0.5);
        gc.setStroke(previewColor);
        gc.setFill(filled ? previewColor : Color.TRANSPARENT);
        gc.setLineDashes(5);

        if (filled) {
            gc.fillPolygon(previewXPoints, previewYPoints, 3);
        }
        gc.strokePolygon(previewXPoints, previewYPoints, 3);
        gc.setLineDashes();
    }

    @Override
    public boolean contains(double x, double y) {
        // Проверяем, находится ли точка внутри треугольника
        return pointInTriangle(x, y, xPoints[0], yPoints[0],
                xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
    }

    // Алгоритм проверки точки в треугольнике (барицентрические координаты)
    private boolean pointInTriangle(double px, double py,
                                    double x1, double y1, double x2, double y2, double x3, double y3) {
        double d1 = sign(px, py, x1, y1, x2, y2);
        double d2 = sign(px, py, x2, y2, x3, y3);
        double d3 = sign(px, py, x3, y3, x1, y1);

        boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(hasNeg && hasPos);
    }

    private double sign(double x1, double y1, double x2, double y2, double x3, double y3) {
        return (x1 - x3) * (y2 - y3) - (x2 - x3) * (y1 - y3);
    }

    @Override
    public String getType() {
        return "TRIANGLE";
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        map.put("centerX", startX);
        map.put("centerY", startY);
        map.put("sideLength", sideLength);
        map.put("color", color.toString());
        map.put("filled", filled);
        return map;
    }

    // Геттеры и сеттеры
    public double getSideLength() {
        return sideLength;
    }

    public void setSideLength(double sideLength) {
        this.sideLength = sideLength;
        calculateVertices(); // Пересчитываем вершины при изменении размера
    }

    public double[] getXPoints() {
        return xPoints.clone();
    }

    public double[] getYPoints() {
        return yPoints.clone();
    }

    @Override
    public String toString() {
        return String.format("Triangle(center=(%.1f, %.1f), side=%.1f, color=%s, filled=%s)",
                startX, startY, sideLength, color, filled);
    }
}