package model;

import com.example.fabrichmetod.AbstrakClass;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class Polygon extends AbstrakClass {
    private int sides;
    private double radius;

    public Polygon(double centerX, double centerY, int sides, double radius,
                   Color color, boolean filled) {
        super(centerX, centerY, color, filled);
        this.sides = Math.max(3, sides);
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setFill(filled ? color : Color.TRANSPARENT);

        double[] xPoints = calculateXPoints();
        double[] yPoints = calculateYPoints();

        if (filled) {
            gc.fillPolygon(xPoints, yPoints, sides);
        }
        gc.strokePolygon(xPoints, yPoints, sides);
    }

    @Override
    public void drawPreview(GraphicsContext gc, double currentX, double currentY) {
        double previewRadius = calculateDistance(startX, startY, currentX, currentY);
        Color previewColor = color.deriveColor(0, 1, 1, 0.5);

        gc.setStroke(previewColor);
        gc.setFill(filled ? previewColor : Color.TRANSPARENT);
        gc.setLineDashes(5);

        double[] xPoints = calculateXPoints(previewRadius);
        double[] yPoints = calculateYPoints(previewRadius);

        if (filled) {
            gc.fillPolygon(xPoints, yPoints, sides);
        }
        gc.strokePolygon(xPoints, yPoints, sides);
        gc.setLineDashes();
    }

    @Override
    public boolean contains(double x, double y) {
        return pointInPolygon(x, y);
    }

    private boolean pointInPolygon(double x, double y) {
        double[] xPoints = calculateXPoints();
        double[] yPoints = calculateYPoints();

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

    private double[] calculateXPoints() {
        return calculateXPoints(radius);
    }

    private double[] calculateXPoints(double radius) {
        double[] xPoints = new double[sides];
        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides;
            xPoints[i] = startX + radius * Math.cos(angle);
        }
        return xPoints;
    }

    private double[] calculateYPoints() {
        return calculateYPoints(radius);
    }

    private double[] calculateYPoints(double radius) {
        double[] yPoints = new double[sides];
        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides;
            yPoints[i] = startY + radius * Math.sin(angle);
        }
        return yPoints;
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

    public int getSides() {
        return sides;
    }

    public void setSides(int sides) {
        this.sides = Math.max(3, sides);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}