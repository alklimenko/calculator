package ru.alklimenko.calculator;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Set;
import java.util.Stack;

/**
 * A class for representing an expression in a form that is convenient for processing
 */
public class Expression extends Stack<Term> {
    private boolean rpn = false;

    /** Is expression is Reverse Polish notation */
     public boolean isRPN() {
        return rpn;
    }

    private void reverse() {
        Collections.reverse(this);
    }

    /**
     * Print expression to console
     */
    public void print() {
        System.out.print(toString());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int idx = this.size() - 1; idx >= 0; --idx) {
            sb.append(get(idx).toString());
        }
        return sb.toString().trim();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * We find the maximum substring in the line starting with index, which is the number
     *
     * @param str   String
     * @param index Initial index
     * @return Returns the first character index after the number, so that the number is str.substring (index, returnIndex) or -1 if at the index is not a number
     */
    static int numberStarts(String str, int index) {
        int index2 = index + 1;
        if (str.charAt(index) == '-' || str.charAt(index) == '+') { //The number can start with a sign
            index2++;
        }
        while (index2 < str.length() && isDouble(str.substring(index, index2))) {
            index2++;
            if (index2 < str.length() && (str.charAt(index2) == 'E' || str.charAt(index2) == 'e')) { //The number in the exponential notation
                index2++;
                if (index2 < str.length() && (str.charAt(index2) == '-' || str.charAt(index2) == '+')) { //There is an exponential sign
                    index2++;
                }
                index2++;
            }
        }
        if (index2 == str.length() && isDouble(str.substring(index, index2))) {   // It if from a cycle left on a condition index2 == str.length ()
            return index2;
        }
        index2--;                                       // And this, if with a new index2 has already received not a number
        return index2 == index ? -1 : index2;
    }


    /**
     * We find the index in the string, that the ending a operator or -1 if there is no operator there
     * @param str String
     * @param index From what position do we look for the operator
     * @return index
     */
    static int operatorStarts(String str, int index) {
        Set<String> operators = Operator.getOperators();
        for(String operator : operators) {
            if (str.startsWith(operator, index)) {
                return index + operator.length();
            }
        }
        return -1;
    }

    /**
     * Parse the string in arithmetic record to Expression.
     *
     * @param str строка вида 5 + 7 * (3 - 1)
     */
    private Expression parse(String str, int index) {
        int index2;
        do {
            // First number must be number or open bracket
            if(isEmpty()) {
                if ((index2 = numberStarts(str, index)) > -1) {
                    Number number = new Number(Double.parseDouble(str.substring(index, index2)));
                    push(number);
                } else if (str.charAt(index) == '(') {
                    push(Operator.get("("));
                    index2 = index + 1;
                } else {
                    throw new InvalidExpressionException("Invalid expression! First term must be number or open bracket!");
                }
            } else {
                if (peek().isType(Term.NUMBER)) { // Number --------------------------->
                    // После числа может быть только оператор
                    if ((index2 = operatorStarts(str, index)) > -1) { // Оператор
                        Operator operation = Operator.get(str.substring(index, index2));
                        push(operation);
                    } else {
                        throw new InvalidExpressionException("Invalid expression! After number mast be only operator!");
                    }
                } else { // Operator ------------------------------>
                    if (((Operator)peek()).isCloseBracket()) { // )
                        // After close bracket mast be only operator!
                        if ((index2 = operatorStarts(str, index)) > -1) { // Оператор
                            Operator operation = Operator.get(str.substring(index, index2));
                            push(operation);
                        } else {
                            throw new InvalidExpressionException("Invalid expression! After close bracket mast be only operator!");
                        }
                    } else if (((Operator)peek()).isOpenBracket()) { // (
                        // After open bracket mast be number or another open bracket!
                        if ((index2 = numberStarts(str, index)) > -1) {
                            Number number = new Number(Double.parseDouble(str.substring(index, index2)));
                            push(number);
                        } else if (str.charAt(index) == '(') {
                            push(Operator.get("("));
                            index2 = index + 1;
                        } else {
                            throw new InvalidExpressionException("Invalid expression! After open bracket mast be number or another open bracket!");
                        }
                    } else if ((index2 = numberStarts(str, index)) > -1) { // After operator (not bracket) goes number
                        Number number = new Number(Double.parseDouble(str.substring(index, index2)));
                        push(number);
                    } else if (str.charAt(index) == '(') { // or open bracket
                        push(Operator.get("("));
                        index2 = index + 1;
                    } else {
                        throw new InvalidExpressionException("Invalid expression! Unable parse expression!");
                    }
                }
            }
            index = index2;
        } while (index2 != str.length());
        reverse();
        return this;
    }

    /**
     * Parse string in arithmetic notation into Expression.
     *
     * @param str строка вида 5 + 7 * (3 - 1)
     */
    public Expression parse(String str) {
        clear();
        rpn = false;
        return parse(str.replaceAll("[ \\t]", ""), 0);
    }

    /**
     *  We transform the expression from an arithmetic notation into a reverse Polish notation
     * @return this
     */
    public Expression toOPN() {
        if (rpn) { // Already in the RPN
            return this;
        }
        rpn = true;
        Expression p = new Expression();  // Выражение в обратной польской нотации (ОПН)
        OperatorStack os = new OperatorStack(); // Стек операций
        do {
            Term t = pop();
            if (t instanceof Number) { // Это число - добавляем в выражение ОПН
                p.push(t);
            } else { // Это оператор
                Operator o = (Operator)t;
                if (o.isCloseBracket()) {  // Закрывающая скобка - берём все операции из стека до
                    do {                        // открывающей скобки и добавляем  выражение ОПН
                        if (os.isEmpty()) {
                            throw new InvalidExpressionException("Invalid expression! Close bracket not found!");
                        }
                        Operator so = os.pop();
                        if(so.isOpenBracket()) {
                            break;
                        }
                        p.push(so);
                    } while(true);
                } else {
                    if (!o.isOpenBracket()) {
                        while (!os.isEmpty() && os.peek().getPriority() >= o.getPriority()) {   // Операции большего или равного приоритета из стека
                            p.push(os.pop());                               // операций добавляем в выражение ОПН
                        }
                    }
                    os.push(o); // Добавляем операцию в стек операций
                }
            }
        } while (!empty());

        while(!os.isEmpty()) {
            p.push(os.pop());
        }

        //p.reverse();
        while (!p.isEmpty()) {
            push(p.pop());
        }
        return this;
    }

    /**
     * Calculate expression in RPN.
     */
    public double calculate() {
        if (!rpn) {
            toOPN();
        }
        try {
            Stack<Double> stack = new Stack<>();
            Stack<Term> tmp = new Stack<>();
            for (int idx = 0; idx < size(); ++idx) {
                tmp.push(get(idx));
            }

            while (!tmp.isEmpty()) {
                Term t = tmp.pop();
                if (t instanceof Number) {
                    stack.push(((Number) t).getValue());
                } else {
                    Operator o = (Operator) t;
                    double b = stack.pop();
                    double a = stack.pop();
                    double c = o.doIt(a, b);
                    stack.push(c);
                }
            }
            if (stack.size() != 1) {
                throw new InvalidExpressionException("Invalid expression! Only result must left on the stack!");
            }
            return stack.pop();
        } catch (EmptyStackException e) {
            throw new InvalidExpressionException("Invalid expression! Unable calculate expression! \r\n" + e.getMessage());
        }
    }

    /**
     * Вычисление арифметического выражения
     * @param str Выражение вида 5 * (5 - 2) ...
     * @return result
     */
    public double calculate(String str) {
        parse(str);
        return calculate();
    }
}
