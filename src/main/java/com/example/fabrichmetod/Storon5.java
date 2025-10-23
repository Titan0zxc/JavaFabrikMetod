package com.example.fabrichmetod;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Storon5 implements AbstrakClass {
    @Override
    public void draw(GraphicsContext gc) {
        double centerX = 150, centerY = 150, radius = 80; // центр фигуры и радиус описанной окружности

        // МАССИВЫ ДЛЯ КООРДИНАТ ВЕРШИН
        double[] xPoints = new double[5];
        double[] yPoints = new double[5];

        // ВЫЧИСЛЕНИЕ КООРДИНАТ ВЕРШИН ПЯТИУГОЛЬНИКА
        for (int i = 0; i < 5; i++) {
            // вычисляем уголов
            double angle = 2 * Math.PI * i / 5 - Math.PI / 2;
            // вычисляем X координаты
            xPoints[i] = centerX + radius * Math.cos(angle);
            // вычисляем Y координаты
            yPoints[i] = centerY + radius * Math.sin(angle);
        }

        // ЗАЛИВКА
        gc.setFill(Color.PURPLE);
        // Полигон
        gc.fillPolygon(xPoints, yPoints, 5);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokePolygon(xPoints, yPoints, 5);
    }

    @Override
    public String descriptor() {
        return "Пятиугольник (5 сторон)";
    }
}