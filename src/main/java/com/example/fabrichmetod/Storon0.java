package com.example.fabrichmetod;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// КОНКРЕТНЫЙ КЛАСС - Круг (0 сторон) реализует интерфейс AbstrakClass
public class Storon0 implements AbstrakClass {
    // РЕАЛИЗАЦИЯ МЕТОДА draw() для круга (из лабы)
    @Override
    public void draw(GraphicsContext gc) {
        // ЗАЛИВКА круга зеленым цветом
        gc.setFill(Color.GREEN);
        gc.fillOval(50, 50, 200, 200); // рисуем заполненный овал

        // КОНТУР круга черным цветом
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2); // толщина линии
        gc.strokeOval(50, 50, 200, 200); // рисуем контур овала
    }
    // РЕАЛИЗАЦИЯ МЕТОДА descriptor() (из лабы п.2)
    @Override
    public String descriptor() {
        return "Круг (0 сторон)";  // возвращаем название фигуры
    }
}