package ru.alklimenko.calculator;

import java.util.HashMap;
import java.util.Set;

public class Operator extends Term {
    private String name;
    private int priority;
    private OperatorAction action;

    private Operator(String name, int priority, OperatorAction action) {
        this.name = name;
        this.priority = priority;
        this.action = action;
    }

    double doIt(double a, double b) {
        return action.doIt(a,b);
    }

    public String toString() {
        return " " + name;
    }

    public void print() {
        System.out.print(toString());
    }

    @Override
    public int getType() {
        return OPERATOR;
    }

    int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    boolean isOpenBracket() {
        return name.equals("(");
    }

    boolean isCloseBracket() {
        return name.equals(")");
    }

    boolean isBracket() {
        return isCloseBracket() || isOpenBracket();
    }

    // Static
    private static final HashMap<String, Operator> operators = new HashMap<>();

    /**
     * Add new operator
     * @param name Operator name
     * @param priority Operator priority
     * @param operatorAction Operator action
     */
    public static void add(String name, int priority, OperatorAction operatorAction) {
        operators.put(name, new Operator(name, priority, operatorAction));
    }

    /**
     * Remove operator
     * @param name Operator name
     */
    public static void remove(String name) {
        operators.remove(name);
    }

    // Standards operators
    static {
        // Parentheses must always be
        add("(", 10, null);
        add(")", 10, null);
        // The following operators are optional :)
        add("+", 20, (a, b) -> a + b);
        add("-", 20, (a, b) -> a - b);
        add("*", 30, (a, b) -> a * b);
        add("/", 30, (a, b) -> a / b);
    }

    /**
     * Check if the operator exists
     * @param name Operator name
     * @return boolean
     */
    public static boolean isOperator(String name) {
        return operators.containsKey(name);
    }

    /**
     * Get an operator by its name
     * @param name Operator name
     * @return Operator
     */
    public static Operator get(String name) {
        return operators.get(name);
    }

    /**
     * Get the names of all the added operators
     * @return Set of strings
     */
    public static Set<String> getOperators() {
        return operators.keySet();
    }
}

