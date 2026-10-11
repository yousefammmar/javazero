import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class Main {
    static BigInteger factorial(int n) {
        BigInteger r = BigInteger.ONE;
        for (int i = 2; i <= n; i++) r = r.multiply(BigInteger.valueOf(i));
        return r;
    }

    static BigInteger fib(int n) {
        BigInteger a = BigInteger.ZERO, b = BigInteger.ONE;
        for (int i = 0; i < n; i++) { BigInteger t = a.add(b); a = b; b = t; }
        return a;
    }

    static int digitSum(BigInteger n) {
        int s = 0;
        for (char c : n.toString().toCharArray()) s += c - '0';
        return s;
    }

    public static void main(String[] args) {
        System.out.println("25! = " + factorial(25));
        System.out.println("50! = " + factorial(50));
        BigInteger f100 = factorial(100);
        System.out.println("100! has " + f100.toString().length() + " digits, digit sum " + digitSum(f100));
        System.out.println("fib(200) = " + fib(200));
        System.out.println("2^200 = " + BigInteger.TWO.pow(200));
        BigInteger a = new BigInteger("123456789012345678901234567890");
        BigInteger b = new BigInteger("987654321098765432109876543210");
        System.out.println("sum " + a.add(b));
        System.out.println("diff " + a.subtract(b));
        System.out.println("prod " + a.multiply(b));
        System.out.println("quot " + b.divide(a) + " rem " + b.mod(a));
        System.out.println("gcd " + a.gcd(b));
        System.out.println("cmp " + a.compareTo(b) + " " + b.compareTo(a) + " " + a.compareTo(a));
        System.out.println("modPow " + BigInteger.valueOf(7).modPow(BigInteger.valueOf(1000), BigInteger.valueOf(13)));
        System.out.println("prime? " + BigInteger.valueOf(1_000_000_007L).isProbablePrime(20) + " " + BigInteger.valueOf(1_000_000_008L).isProbablePrime(20));
        System.out.println("negate/abs " + a.negate().abs().equals(a) + " signum " + a.negate().signum());
        System.out.println("hex " + new BigInteger("255").toString(16) + " parse " + new BigInteger("ff", 16));
        System.out.println("long overflow: " + (Long.MAX_VALUE + 1) + " vs " + BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));
        try {
            BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE).longValueExact();
        } catch (ArithmeticException e) {
            System.out.println("longValueExact: " + e.getMessage());
        }
        System.out.println("Math.addExact catch:");
        try {
            Math.addExact(Integer.MAX_VALUE, 1);
        } catch (ArithmeticException e) {
            System.out.println("  " + e.getMessage());
        }
        BigDecimal x = new BigDecimal("0.1"), y = new BigDecimal("0.2");
        System.out.println("0.1+0.2 double: " + (0.1 + 0.2) + ", BigDecimal: " + x.add(y));
        BigDecimal price = new BigDecimal("19.99");
        BigDecimal total = price.multiply(BigDecimal.valueOf(3)).multiply(new BigDecimal("1.0825"));
        System.out.println(total + " -> " + total.setScale(2, RoundingMode.HALF_UP) + " / " + total.setScale(0, RoundingMode.DOWN));
        System.out.println(BigDecimal.ONE.divide(new BigDecimal("7"), 12, RoundingMode.HALF_EVEN));
        System.out.println(new BigDecimal("2.50").equals(new BigDecimal("2.5")) + " " + new BigDecimal("2.50").compareTo(new BigDecimal("2.5")));
    }
}
