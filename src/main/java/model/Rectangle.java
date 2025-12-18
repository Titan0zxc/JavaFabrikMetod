package model;

import com.example.fabrichmetod.AbstrakClass;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class Rectangle extends AbstrakClass {
    private double width, height;

    public Rectangle(double x, double y, double width, double height,
                     Color color, boolean filled) {
        super(x, y, color, filled);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setFill(filled ? color : Color.TRANSPARENT);

        if (filled) {
            gc.fillRect(startX, startY, width, height);
        }
        gc.strokeRect(startX, startY, width, height);
    }

    @Override
    public void drawPreview(GraphicsContext gc, double currentX, double currentY) {
        double previewWidth = currentX - startX;
        double previewHeight = currentY - startY;
        Color previewColor = color.deriveColor(0, 1, 1, 0.5);

        gc.setStroke(previewColor);
        gc.setFill(filled ? previewColor : Color.TRANSPARENT);
        gc.setLineDashes(5);

        if (filled) {
            gc.fillRect(startX, startY, previewWidth, previewHeight);
        }
        gc.strokeRect(startX, startY, previewWidth, previewHeight);
        gc.setLineDashes();
    }

    @Override
    public boolean contains(double x, double y) {
        boolean withinX = x >= Math.min(startX, startX + width) &&
                x <= Math.max(startX, startX + width);
        boolean withinY = y >= Math.min(startY, startY + height) &&
                y <= Math.max(startY, startY + height);
        return withinX && withinY;
    }

    @Override
    public String getType() {
        return "RECTANGLE";
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        map.put("x", startX);
        map.put("y", startY);
        map.put("width", width);
        map.put("height", height);
        map.put("color", color.toString());
        map.put("filled", filled);
        return map;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}