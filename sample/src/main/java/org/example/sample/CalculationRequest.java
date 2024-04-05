package org.example.sample;

/**
 * 연산할 값이 담긴 객체
 *
 * 개선 사항 3 : 사용자로부터 받는 값을 변수가 아닌 객체로 관리한다.
 */
public class CalculationRequest {

    private final long num1;
    private final long num2;
    private final String operator;

    public CalculationRequest(String[] parts) {
        // 'vo 안의 변수들은 값이 항상 유효하다'라는 특징을 지키기 위해, 유효성 검증하는 로직 추가
        if(parts.length != 3) {
            throw new BadRequestException();
        }
        String operator = parts[1];
        if(operator.length() != 1 // +-와 같은 값 방지
            || isInvalidOperator(operator)) {
            throw new InvalidOperatorException();
        }

        this.num1 = Long.parseLong(parts[0]);
        this.num2 = Long.parseLong(parts[2]);
        this.operator = operator;
    }

    private static boolean isInvalidOperator(String operator) {
        return !operator.equals("+")
                && !operator.equals("-")
                && !operator.equals("*")
                && !operator.equals("/");
    }

    public long getNum1() {
        return num1;
    }

    public long getNum2() {
        return num2;
    }

    public String getOperator() {
        return operator;
    }
}
