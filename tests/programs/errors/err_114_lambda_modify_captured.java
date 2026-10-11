public class Main {
    public static void main(String[] args) {
        int total = 0;
        Runnable r = () -> { total += 1; };
        r.run();
        System.out.println(total);
    }
}
