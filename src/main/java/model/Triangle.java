package model;

import com.example.fabrichmetod.AbstrakClass;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class Triangle extends AbstrakClass {
    private double sideLength;

    public Triangle(double centerX, double centerY, double sideLength,
                    Color color, boolean filled) {
        super(centerX, centerY, color, filled);
        this.sideLength = sideLength;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setFill(filled ? color : Color.TRANSPARENT);

        double[] xPoints = calculateXPoints();
        double[] yPoints = calculateYPoints();

        if (filled) {
            gc.fillPolygon(xPoints, yPoints, 3);
        }
        gc.strokePolygon(xPoints, yPoints, 3);
    }

    @Override
    public void drawPreview(GraphicsContext gc, double currentX, double currentY) {
        double previewSideLength = calculateDistance(startX, startY, currentX, currentY) * 2;
        Color previewColor = color.deriveColor(0, 1, 1, 0.5);

        gc.setStroke(previewColor);
        gc.setFill(filled ? previewColor : Color.TRANSPARENT);
        gc.setLineDashes(5);

        double[] xPoints = calculateXPoints(previewSideLength);
        double[] yPoints = calculateYPoints(previewSideLength);

        if (filled) {
            gc.fillPolygon(xPoints, yPoints, 3);
        }
        gc.strokePolygon(xPoints, yPoints, 3);
        gc.setLineDashes();
    }

    @Override
    public boolean contains(double x, double y) {
        double[] xPoints = calculateXPoints();
        double[] yPoints = calculateYPoints();
        return pointInTriangle(x, y, xPoints[0], yPoints[0],
                xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
    }

    // Алгоритм проверки точки в треугольнике
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

    private double[] calculateXPoints() {
        return calculateXPoints(sideLength);
    }

    private double[] calculateXPoints(double side) {
        double height = (Math.sqrt(3) / 2) * side;
        return new double[] {
                startX,                    // Верхняя вершина
                startX - side / 2,         // Левая нижняя
                startX + side / 2          // Правая нижняя
        };
    }

    private double[] calculateYPoints() {
        return calculateYPoints(sideLength);
    }

    private double[] calculateYPoints(double side) {
        double height = (Math.sqrt(3) / 2) * side;
        return new double[] {
                startY - (2.0/3.0) * height, // Верхняя вершина
                startY + height / 3,         // Левая нижняя
                startY + height / 3          // Правая нижняя
        };
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

    public double getSideLength() {
        return sideLength;
    }

    public void setSideLength(double sideLength) {
        this.sideLength = sideLength;
    }
}