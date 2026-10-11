public class Main {
    static int f() {
        return 1;
        System.out.println("never");
    }
    public static void main(String[] args) {
        System.out.println(f());
    }
}
