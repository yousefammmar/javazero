public class Main {
    public static void main(String[] args) {
        int v = 1;
        for (int i = 0; i < 40; i++) {
            v = Math.multiplyExact(v, 2);
            System.out.println(i + " " + v);
        }
    }
}
