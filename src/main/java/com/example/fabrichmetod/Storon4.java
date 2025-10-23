package com.example.fabrichmetod;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Storon4 implements AbstrakClass {
    @Override
    public void draw(GraphicsContext gc) {
        // ЗАЛИВКА
        gc.setFill(Color.YELLOW);
        gc.fillRect(75, 75, 150, 150);

        // КОНТУР
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        // рисуем контур
        gc.strokeRect(75, 75, 150, 150);
    }

    @Override
    public String descriptor() {
        return "Квадрат (4 стороны)";
    }
}