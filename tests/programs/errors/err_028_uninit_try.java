public class Main {
    public static void main(String[] args) {
        int v;
        try {
            v = Integer.parseInt("5");
        } catch (NumberFormatException e) {
            System.out.println("bad");
        }
        System.out.println(v);
    }
}
