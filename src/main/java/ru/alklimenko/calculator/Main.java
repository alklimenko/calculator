package ru.alklimenko.calculator;

import java.util.Scanner;

public class Main {
    private static final Scanner scanInput = new Scanner(System.in);

    private static String getLine() {
        String data;

        data = scanInput.nextLine();

        return data;
    }

    public static void main(String[] args) {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        Expression exp = new Expression();
        Operator.add("^", 4, Math::pow);
        System.out.println("Enter arithmetic expression for calculation");
        System.out.println("Empty string assume exit");
        do {
            System.out.print(">");
            String str = getLine();
            if (str.trim().equals("")) {
                break;
            }
            try {
                System.out.println(">" + exp.calculate(str));
            } catch(InvalidExpressionException e) {
                System.out.println(e.getMessage());
                System.out.println();
            }
        } while(true);
    }
}
