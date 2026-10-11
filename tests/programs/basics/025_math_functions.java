public class Main {
    public static void main(String[] args) {
        double[] xs = {2.5, 3.5, -2.5, -3.5, 2.4, 2.6, -2.4, -2.6, 0.5, -0.5, 1.5, -1.5, 0.49999999999999994, -0.0, 1e15 + 0.5, 4503599627370497.0};
        for (double x : xs) {
            System.out.println(x + ": round=" + Math.round(x) + " rint=" + Math.rint(x) + " floor=" + Math.floor(x) + " ceil=" + Math.ceil(x) + " (int)=" + (int) x + " abs=" + Math.abs(x) + " signum=" + Math.signum(x));
        }
        System.out.println(Math.round(2.5f) + " " + Math.round(-2.5f) + " " + Math.round(Float.NaN) + " " + Math.round(1e20) + " " + Math.round(-1e20) + " " + Math.round(1e10f));
        System.out.println(Math.abs(Integer.MIN_VALUE) + " " + Math.abs(Long.MIN_VALUE) + " " + Math.abs(-5) + " " + Math.abs(-5L) + " " + Math.abs(-5.5f));
        System.out.println(Math.pow(2, 10) + " " + Math.pow(2, -1) + " " + Math.pow(2, 0.5) + " " + Math.pow(-8, 1.0 / 3) + " " + Math.pow(0, 0) + " " + Math.pow(10, 15) + " " + Math.pow(10, 16) + " " + Math.pow(2, 62));
        System.out.println(Math.sqrt(16) + " " + Math.sqrt(2) + " " + Math.sqrt(0) + " " + Math.cbrt(27) + " " + Math.cbrt(-8) + " " + Math.hypot(3, 4) + " " + Math.hypot(1, 1));
        System.out.println(Math.max(1, 2) + " " + Math.min(-1, -2) + " " + Math.max(1L, 2) + " " + Math.min(1.5, 1) + " " + Math.max(Double.NaN, 1) + " " + Math.max(-0.0f, 0.0f));
        System.out.println(Math.floorDiv(-7, 2) + " " + Math.floorDiv(7, -2) + " " + Math.floorMod(-7, 2) + " " + Math.floorMod(7, -2) + " " + (-7 / 2) + " " + (-7 % 2));
        System.out.println(Math.ceil(-0.5) + " " + Math.floor(-0.5) + " " + Math.ceil(0.2) + " " + Math.floor(5) + " " + Math.ceil(7 / 2) + " " + Math.ceil(7 / 2.0));
        System.out.println(Math.sin(0) + " " + Math.cos(0) + " " + Math.tan(0) + " " + Math.atan(1) * 4 + " " + Math.atan2(1, 1) + " " + Math.atan2(-1, -1));
        System.out.println(Math.toDegrees(Math.PI) + " " + Math.toRadians(180) + " " + Math.toDegrees(1) + " " + Math.toRadians(90));
        System.out.println(Math.exp(0) + " " + Math.log(1) + " " + Math.log10(1000) + " " + Math.log10(0.001) + " " + Math.log(0) + " " + Math.log(-1) + " " + Math.exp(1) + " " + Math.log(Math.E));
        System.out.println(Math.addExact(1, 2) + " " + Math.subtractExact(5L, 7L) + " " + Math.negateExact(5) + " " + Math.incrementExact(9) + " " + Math.multiplyExact(1 << 15, 1 << 15));
        try { Math.negateExact(Integer.MIN_VALUE); } catch (ArithmeticException e) { System.out.println(e.getMessage()); }
        try { Math.incrementExact(Integer.MAX_VALUE); } catch (ArithmeticException e) { System.out.println(e.getMessage()); }
        try { Math.multiplyExact(1 << 16, 1 << 15); } catch (ArithmeticException e) { System.out.println(e.getMessage()); }
        System.out.println(Math.floorMod(-1L, 5L) + " " + Math.clamp(15, 0, 10) + " " + Math.clamp(-5L, 0, 10) + " " + Math.clamp(2.5, 0.0, 1.0));
        System.out.println(Math.sqrt(1e300 * 1e300) + " " + Math.sqrt(-0.0) + " " + Math.pow(2, 1024) + " " + Math.pow(-2, 3) + " " + Math.pow(-2, 2) + " " + Math.pow(2, 0.5) * Math.pow(2, 0.5));
        System.out.println(Math.ulp(1.0) + " " + Math.nextUp(1.0f) + " " + Math.nextAfter(1.0, 0) + " " + Math.getExponent(1024.0) + " " + Math.scalb(1.0, 4) + " " + Math.copySign(3, -0.0) + " " + Math.IEEEremainder(10, 3));
        System.out.println((int) Math.signum(-9.0) + " " + (int) Math.floor(-1.1) + " " + (int) Math.ceil(-1.9) + " " + (long) Math.pow(2, 40) + " " + (int) Math.sqrt(99) + " " + (int) (Math.random() * 0));
        System.out.println(Math.fma(2.0, 3.0, 1.0) + " " + Math.absExact(-4) + " " + Math.ceilDiv(7, 2) + " " + Math.ceilDiv(-7, 2) + " " + Math.ceilMod(7, 2));
        long sumSq = 0;
        for (int i = 1; i <= 100; i++) sumSq += (long) Math.pow(i, 2);
        System.out.println(sumSq);
        double hyp = Math.sqrt(Math.pow(5, 2) + Math.pow(12, 2));
        System.out.println(hyp + " " + (hyp == 13) + " " + Math.sin(Math.PI) + " " + Math.cos(Math.PI / 2) + " " + Math.sin(Math.PI / 6));
        System.out.printf("%.4f %.4f %.4f%n", Math.sinh(1), Math.cosh(1), Math.tanh(1));
    }
}
