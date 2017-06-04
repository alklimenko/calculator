package ru.alklimenko.calculator;

import org.junit.Test;

public class OperatorStackTest {
    @Test
    public void pushTest() {
        OperatorStack os = new OperatorStack();
        os.push(Operator.get("+"));
        os.push(Operator.get("("));
        os.push(Operator.get("*"));
        os.push(Operator.get("-"));
    }
}
