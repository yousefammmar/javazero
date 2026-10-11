public class Main {
    static String name(int d) {
        switch (d) {
            case 1: return "one";
            case 2: return "two";
        }
    }
    public static void main(String[] args) {
        System.out.println(name(1));
    }
}
