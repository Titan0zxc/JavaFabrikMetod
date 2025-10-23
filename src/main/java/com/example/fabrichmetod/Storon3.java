package com.example.fabrichmetod;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Storon3 implements AbstrakClass {
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillPolygon(
                new double[]{150, 100, 200},  // X координаты
                new double[]{100, 200, 200},  // Y координаты
                3  // количество точек
        );

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokePolygon(
                new double[]{150, 100, 200},
                new double[]{100, 200, 200},
                3
        );
    }

    @Override
    public String descriptor() {
        return "Треугольник (3 стороны)";
    }
}