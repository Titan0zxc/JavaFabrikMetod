package com.example.fabrichmetod;

import javafx.scene.canvas.GraphicsContext;

// ИНТЕРФЕЙС AbstrakClass (из лабы Шаг 1)
public interface AbstrakClass {
    // АБСТРАКТНЫЙ МЕТОД ДЛЯ РИСОВАНИЯ ФИГУРЫ
    void draw(GraphicsContext gc);

    // МЕТОД descriptor() для вывода названия фигуры (из лабы п.2)
    String descriptor();
}