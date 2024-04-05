package org.example.sample;

import java.util.Scanner;

public class CalculationRequestReader {

    /**
     * 사용자 입력을 받는다.
     *
     * 개선 사항 2 : Main에서 직접 연산하지 않고 메소드로 분리했다.
     *
     * @return
     */
    public CalculationRequest read() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter two numbers and an operator (e.g. 1 + 2) : ");
        String result = scanner.nextLine();
        String[] parts = result.split(" ");
        return new CalculationRequest(parts);
    }
}
