public class Main {
    public static void main(String[] args) {
        throw new RuntimeException("x");
        System.out.println("after");
    }
}
