public class Main {
    static int[] build(int n) { System.out.println("build " + n); return new int[n]; }
    public static void main(String[] args) {
        for (int n = 2; n >= -1; n--) System.out.println(build(n).length);
    }
}
