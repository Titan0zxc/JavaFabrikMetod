package com.example.fabrichmetod;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Storon2 implements AbstrakClass {
    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        // Вертикальная линия
        gc.strokeLine(100, 100, 100, 200);
        // Горизонтальная линия
        gc.strokeLine(100, 100, 200, 100);
    }

    @Override
    public String descriptor() {
        return "Угол (2 стороны)";
    }
}