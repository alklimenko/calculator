package ru.alklimenko.calculator;

class InvalidExpressionException extends RuntimeException {

    InvalidExpressionException(String s) {
        super(s);
    }
}
