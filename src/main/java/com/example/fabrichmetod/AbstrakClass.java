package com.example.fabrichmetod;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Map;

public abstract class AbstrakClass {
    // Защищенные поля (были private в старом проекте)
    protected double startX, startY;
    protected Color color;
    protected boolean filled;

    // Конструктор (новый)
    public AbstrakClass(double startX, double startY, Color color, boolean filled) {
        this.startX = startX;
        this.startY = startY;
        this.color = color;
        this.filled = filled;
    }

    // Абстрактные методы для переопределения в дочерних классах
    public abstract void draw(GraphicsContext gc);
    public abstract void drawPreview(GraphicsContext gc, double endX, double endY);
    public abstract boolean contains(double x, double y);

    // Методы для сериализации в JSON (новые)
    public abstract String getType();
    public abstract Map<String, Object> toMap();

    // Геттеры и сеттеры (добавляем)
    public double getStartX() { return startX; }
    public void setStartX(double startX) { this.startX = startX; }

    public double getStartY() { return startY; }
    public void setStartY(double startY) { this.startY = startY; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public boolean isFilled() { return filled; }
    public void setFilled(boolean filled) { this.filled = filled; }

    // Вспомогательный метод для вычисления расстояния
    protected double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}