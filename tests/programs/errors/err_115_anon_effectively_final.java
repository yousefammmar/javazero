public class Main {
    public static void main(String[] args) {
        int n = 1;
        n = 2;
        Runnable r = new Runnable() {
            public void run() { System.out.println(n); }
        };
        r.run();
    }
}
