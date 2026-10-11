public class Main {
    public static void main(String[] args) {
        System.out.println(Math.pow(2, 10) + " " + Math.pow(2, -1) + " " + Math.pow(0, 0) + " " + Math.pow(-8, 1.0 / 3) + " " + Math.pow(-2, 3));
        System.out.println(Math.pow(0, -1) + " " + Math.pow(-1, Double.POSITIVE_INFINITY) + " " + Math.pow(Double.NaN, 0) + " " + Math.pow(10, 308) + " " + Math.pow(10, 309));
        System.out.println(Math.sqrt(-1) + " " + Math.sqrt(2) + " " + Math.sqrt(0.0) + " " + Math.sqrt(1e100));
        System.out.println(Math.cbrt(27) + " " + Math.cbrt(-8) + " " + Math.cbrt(2));
        System.out.println(Math.hypot(3, 4) + " " + Math.hypot(1, 1) + " " + Math.hypot(1e200, 1e200));
        System.out.println(Math.atan2(1, 1) + " " + Math.atan2(-1, -1) + " " + Math.atan2(0, -1) + " " + Math.atan2(0.0, 0.0));
        System.out.println(Math.exp(0) + " " + Math.exp(1) + " " + Math.exp(-1) + " " + Math.exp(710));
        System.out.println(Math.log(Math.E) + " " + Math.log(10) + " " + Math.log(0) + " " + Math.log(-1) + " " + Math.log10(1000) + " " + Math.log10(2));
        System.out.println(Math.toRadians(180) + " " + Math.toDegrees(Math.PI) + " " + Math.toRadians(45));
        System.out.println(Math.sin(0) + " " + Math.cos(0) + " " + Math.tan(0));
        System.out.println(Math.sin(Math.PI / 2) + " " + Math.cos(Math.PI / 2));
        System.out.println(Math.asin(1) + " " + Math.acos(0) + " " + Math.atan(1) + " " + Math.asin(2));
        System.out.println(Math.max(1, 2L) + " " + Math.max(1, 2.5) + " " + Math.min(-0.0, 0.0) + " " + Math.max(-0.0, 0.0) + " " + Math.min(3, 2.0f) + " " + Math.max(Double.NaN, 1));
        System.out.println(Math.PI + " " + Math.E);
        System.out.println(Math.addExact(1, 2) + " " + Math.multiplyExact(100000L, 100000L));
        try { Math.addExact(Integer.MAX_VALUE, 1); } catch (ArithmeticException e) { System.out.println(e.getMessage()); }
        try { Math.toIntExact(1L << 40); } catch (ArithmeticException e) { System.out.println(e.getMessage()); }
        try { Math.multiplyExact(1L << 62, 4L); } catch (ArithmeticException e) { System.out.println(e.getMessage()); }
    }
}
