public class Main {
    public static void main(String[] args) {
        try {
            int v = Integer.parseInt("12");
        } catch (NumberFormatException e) {
            System.out.println("bad");
        }
        System.out.println(v);
    }
}
