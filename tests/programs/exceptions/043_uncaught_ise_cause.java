public class Main {
    static class Conn implements AutoCloseable {
        public void close() { System.out.println("conn closed"); }
    }
    static void run() {
        try (Conn c = new Conn()) {
            System.out.println("running");
            throw new IllegalStateException("connection lost", new java.io.IOException("socket"));
        }
    }
    public static void main(String[] args) {
        System.out.println("begin");
        run();
    }
}
