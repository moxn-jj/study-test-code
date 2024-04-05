package org.example.sample;

public class Calculator {

    /**
     * 연산을 한다.
     *
     * 개선 사항 1 : Main에서 직접 연산하지 않고 메소드로 분리했다.
     *
     * @param num1
     * @param operator
     * @param num2
     * @return
     */
    public long calculate(long num1, String operator, long num2) {

        return switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> num1 / num2;
            default -> throw new InvalidOperatorException();
        };
    }
}
