package org.example.sample;

import java.util.Scanner;

public class SampleApplication {

    public static void main(String[] args) {
        CalculationRequest calculationRequest = new CalculationRequestReader().read(); // 개선 사항 3 적용
        long answer = new Calculator().calculate(
                calculationRequest.getNum1()
                , calculationRequest.getOperator()
                , calculationRequest.getNum2()); // 개선 사항 1 적용
        System.out.println(answer);
    }
}
