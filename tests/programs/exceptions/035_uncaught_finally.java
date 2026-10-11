public class Main {
    static void work() {
        try {
            System.out.println("working");
            throw new IllegalStateException("work failed");
        } finally {
            System.out.println("cleanup");
        }
    }
    public static void main(String[] args) {
        try { work(); } finally { System.out.println("main finally"); }
        System.out.println("unreachable");
    }
}
