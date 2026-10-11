public class Main {
    static int divide(int a, int b) { return a / b; }
    public static void main(String[] args) {
        System.out.println("start");
        System.out.println(divide(10, 2));
        try { divide(1, 0); } catch (ArithmeticException e) { System.out.println("caught " + e.getMessage()); }
        System.out.println(5.0 / 0);
        System.out.println(divide(7, 0));
        System.out.println("never printed");
    }
}
