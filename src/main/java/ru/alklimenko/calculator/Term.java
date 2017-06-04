package ru.alklimenko.calculator;

abstract class Term
{
    static final int NUMBER = 0;
    static final int OPERATOR = 1;
    abstract public void print();
    protected abstract int getType();
    boolean isType(int type) {
        return type == getType();
    }
}
