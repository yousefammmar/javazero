import java.math.BigInteger;
public class Main {
    public static void main(String[] args) {
        BigInteger f = BigInteger.ONE;
        for (int i = 2; i <= 30; i++) f = f.multiply(BigInteger.valueOf(i));
        System.out.println("30! = " + f);
        System.out.println(f.bitLength() + " " + f.toString(16) + " " + f.toString(2).length() + " " + f.signum());
        BigInteger b = new BigInteger("123456789012345678901234567890");
        System.out.println(b.pow(3));
        System.out.println(b.mod(BigInteger.valueOf(97)) + " " + b.remainder(BigInteger.valueOf(-97)) + " " + b.negate().mod(BigInteger.valueOf(97)) + " " + b.negate().remainder(BigInteger.valueOf(97)));
        System.out.println(b.gcd(f) + " " + BigInteger.valueOf(48).gcd(BigInteger.valueOf(18)));
        System.out.println(b.compareTo(f) + " " + f.compareTo(b) + " " + b.compareTo(b) + " " + b.equals(new BigInteger("123456789012345678901234567890")));
        System.out.println(BigInteger.valueOf(255).toString(2) + " " + BigInteger.valueOf(255).toString(16) + " " + BigInteger.valueOf(-255).toString(36) + " " + new BigInteger("ff", 16) + " " + new BigInteger("-1010", 2));
        System.out.println(BigInteger.TWO.pow(100) + " " + BigInteger.TEN.pow(20).subtract(BigInteger.ONE));
        System.out.println(BigInteger.valueOf(3).modPow(BigInteger.valueOf(200), BigInteger.valueOf(1000007)) + " " + BigInteger.valueOf(3).modInverse(BigInteger.valueOf(11)));
        BigInteger[] qr = f.divideAndRemainder(b);
        System.out.println(qr[0] + " " + qr[1]);
        System.out.println(b.divide(BigInteger.valueOf(-7)) + " " + b.negate().divide(BigInteger.valueOf(7)));
        System.out.println(b.longValue() + " " + b.intValue() + " " + BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE) + " " + b.doubleValue());
        System.out.println(BigInteger.valueOf(97).isProbablePrime(20) + " " + BigInteger.valueOf(91).isProbablePrime(20) + " " + BigInteger.valueOf(100).sqrt() + " " + b.max(f).equals(f) + " " + b.abs().min(BigInteger.ZERO));
        System.out.println(BigInteger.ONE.shiftLeft(70) + " " + b.shiftRight(50) + " " + b.and(BigInteger.valueOf(0xffff)) + " " + b.testBit(0) + " " + b.bitCount());
        BigInteger fib1 = BigInteger.ZERO, fib2 = BigInteger.ONE;
        for (int i = 0; i < 150; i++) { BigInteger t = fib1.add(fib2); fib1 = fib2; fib2 = t; }
        System.out.println(fib1);
        System.out.println(b.hashCode() + " " + BigInteger.valueOf(5).hashCode() + " " + BigInteger.ZERO.hashCode());
        try { b.divide(BigInteger.ZERO); } catch (ArithmeticException e) { System.out.println(e.getMessage()); }
        try { new BigInteger("12x"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
    }
}
