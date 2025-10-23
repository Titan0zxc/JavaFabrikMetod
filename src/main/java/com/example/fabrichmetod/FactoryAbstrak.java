package com.example.fabrichmetod;

// ФАБРИЧНЫЙ МЕТОД
public class FactoryAbstrak {

    // ФАБРИЧНЫЙ МЕТОД
    public AbstrakClass createShape(int numberOfSides) {
        if (numberOfSides == 0) {
            return new Storon0();
        } else if (numberOfSides == 1) {
            return new Storon1();
        } else if (numberOfSides == 2) {
            return new Storon2();
        } else if (numberOfSides == 3) {
            return new Storon3();
        } else if (numberOfSides == 4) {
            return new Storon4();
        } else if (numberOfSides == 5) {
            return new Storon5();
        } else {
            return null;
        }
    }
}