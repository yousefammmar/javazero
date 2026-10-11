public class Main {
    public static void main(String[] args) {
        double[] xs = {-2.5, -1.5, -0.5, -0.4, 0.0, 0.4, 0.5, 1.5, 2.5, -2.51, 2.49, 1e10 + 0.5};
        for (double x : xs) {
            System.out.println(x + " round=" + Math.round(x) + " rint=" + Math.rint(x) + " floor=" + Math.floor(x) + " ceil=" + Math.ceil(x) + " abs=" + Math.abs(x));
        }
        System.out.println(Math.round(-0.5f) + " " + Math.round(2.5f) + " " + Math.round(Double.NaN) + " " + Math.round(1e30));
        System.out.println(Math.ceil(-0.2));
        System.out.println(Math.floor(-0.0));
        System.out.println(Math.abs(Integer.MIN_VALUE));
        System.out.println(Math.abs(Long.MIN_VALUE));
        System.out.println(Math.abs(-0.0));
        System.out.println(Math.signum(-3.2) + " " + Math.signum(0.0) + " " + Math.signum(5f));
        System.out.println(Math.floorDiv(-7, 2) + " " + Math.floorDiv(7, -2) + " " + Math.floorMod(-7, 3) + " " + Math.floorMod(7, -3) + " " + (-7 / 2) + " " + (-7 % 3));
        System.out.println(Math.floorMod(-7L, 3) + " " + Math.floorDiv(-7L, 2L));
    }
}
