package com.example.fabrichmetod;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// КОНКРЕТНЫЙ КЛАСС - Отрезок (1 сторона)
public class Storon1 implements AbstrakClass {
    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);
        gc.strokeLine(50, 150, 250, 150);
    }

    @Override
    public String descriptor() {
        return "Отрезок (1 сторона)";
    }
}