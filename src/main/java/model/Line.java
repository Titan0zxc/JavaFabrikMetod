package model;


import com.example.fabrichmetod.AbstrakClass;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class Line extends AbstrakClass {
    private double endX, endY;

    public Line(double startX, double startY, double endX, double endY, Color color) {
        super(startX, startY, color, false); // Линия не может быть залита
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokeLine(startX, startY, endX, endY);
    }

    @Override
    public void drawPreview(GraphicsContext gc, double currentX, double currentY) {
        gc.setStroke(color.deriveColor(0, 1, 1, 0.5));
        gc.setLineWidth(2);
        gc.setLineDashes(5);
        gc.strokeLine(startX, startY, currentX, currentY);
        gc.setLineDashes();
    }

    @Override
    public boolean contains(double x, double y) {
        double distance = pointToLineDistance(x, y, startX, startY, endX, endY);
        return distance < 5; // Порог 5 пикселей
    }

    private double pointToLineDistance(double px, double py,
                                       double x1, double y1, double x2, double y2) {
        double A = px - x1;
        double B = py - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = (len_sq != 0) ? dot / len_sq : -1;

        double xx, yy;
        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        return calculateDistance(px, py, xx, yy);
    }

    @Override
    public String getType() {
        return "LINE";
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        map.put("startX", startX);
        map.put("startY", startY);
        map.put("endX", endX);
        map.put("endY", endY);
        map.put("color", color.toString());
        return map;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }
}