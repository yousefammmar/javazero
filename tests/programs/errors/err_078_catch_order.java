public class Main {
    public static void main(String[] args) {
        try {
            int x = 5 / 0;
        } catch (Exception e) {
            System.out.println("e");
        } catch (ArithmeticException e) {
            System.out.println("a");
        }
    }
}
