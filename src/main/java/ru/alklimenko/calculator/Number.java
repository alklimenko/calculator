package ru.alklimenko.calculator;

public class Number extends Term {
    private final double value;

    Number(double value) {
        this.value = value;
    }

    double getValue() {
        return value;
    }

    public String toString() {
        return " " + Double.toString(value);
    }

    public void print() {
        System.out.print(toString());
    }

    @Override
    public int getType() {
        return NUMBER;
    }
}
