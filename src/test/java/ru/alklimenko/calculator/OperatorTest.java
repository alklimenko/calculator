package ru.alklimenko.calculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OperatorTest {

    @Test
    public void OperatorPlusTest() {
        assertEquals(8, Operator.get("+").doIt(3,5), 1e-10);
    }

    @Test
    public void OperatorMinusTest() {
        assertEquals(8, Operator.get("-").doIt(13,5), 1e-10);
    }

    @Test
    public void OperatorMultiplyTest() {
        assertEquals(15, Operator.get("*").doIt(3,5), 1e-10);
    }

    @Test
    public void OperatorDivTest() {
        assertEquals(2.5, Operator.get("/").doIt(10,4), 1e-10);
    }

    @Test
    public void OperatorAddTest() {
        Operator.add("^", 4, Math::pow);
        assertEquals(16, Operator.get("^").doIt(2,4), 1e-10);
    }
}
