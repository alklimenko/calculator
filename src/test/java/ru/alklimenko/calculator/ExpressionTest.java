package ru.alklimenko.calculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
public class ExpressionTest {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Expression exp = new Expression();
    @Test
    public void numberStartsTest() {
        assertEquals(3, Expression.numberStarts("123", 0));
        assertEquals(4, Expression.numberStarts("-123", 0));
        assertEquals(7, Expression.numberStarts("123e+11", 0));
        assertEquals(8, Expression.numberStarts("-1.12e-6", 0));
        assertEquals(7, Expression.numberStarts("-1.12e6", 0));
        assertEquals(12, Expression.numberStarts("abbs-1.12e-6accd", 4));
        assertEquals(-1, Expression.numberStarts("abbs-1.12e-6accd", 3));
    }

    @Test
    public void operatorStartsTest() {
        assertEquals(1, Expression.operatorStarts("+", 0));
        assertEquals(3, Expression.operatorStarts("12+12", 2));
        boolean eqeq = Operator.isOperator("==");
        if (!eqeq) {
            Operator.add("==", 20, (a, b) -> a == b ? 1 : 0);
        }
        assertEquals(4, Expression.operatorStarts("12==12", 2));
        if (!eqeq) {
            Operator.remove("==");
        }
    }

    @Test
    public void parseTest() {
        exp.parse("3+    5*7  ");
        assertEquals("3.0 + 5.0 * 7.0", exp.toString());
        exp.parse("3-(6-3)*2");
        assertEquals("3.0 - ( 6.0 - 3.0 ) * 2.0", exp.toString());
        exp.parse("((3-2)*(4+7)/(8+4))*(3+2)");
        assertEquals("( ( 3.0 - 2.0 ) * ( 4.0 + 7.0 ) / ( 8.0 + 4.0 ) ) * ( 3.0 + 2.0 )", exp.toString());
    }

    @Test
    public void computeTest() {
        assertEquals(25, exp.parse("5*(3+2)").calculate(), 1e-8);
        assertEquals(4.583333, exp.parse("((3-2)*(4+7)/(8+4))*(3+2)").calculate(), 1e-4);
        assertEquals(38, exp.calculate("3+5*7"), 1e-8);
        assertEquals(20, exp.calculate("-4*(-1-4)"), 1e-8);
    }

    @Test
    public void parseNegTest() {
        assertEquals("-4.0 * 2.0", exp.parse("-4 * 2").toString());
        assertEquals("-4.0 * ( -1.0 - 4.0 )", exp.parse("-4*(-1-4)").toString());
    }
}
