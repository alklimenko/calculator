package ru.alklimenko.calculator;

import java.util.ArrayList;

/**
 * Стек операций для преобразования выражения в ОПН
 */
class OperatorStack {
    private final ArrayList<Operator> stack = new ArrayList<>();

    void push(Operator o) {
        stack.add(o);
        int index = stack.size() - 1;
        while(index > 0 && peek().getPriority() > o.getPriority() && !peek().isOpenBracket()) {
            stack.set(index, stack.get(index - 1));
            stack.set(index - 1, o);
            index--;
        }
    }

    Operator peek() {
        return stack.get(stack.size() - 1);
    }

    Operator pop() {
        Operator o = peek();
        stack.remove(stack.size() - 1);
        return o;
    }

    boolean isEmpty() {
        return stack.isEmpty();
    }
}
