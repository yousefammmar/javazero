public class Main {
    static int find(int[] a, int t) {
        int i = 0;
        while (i < a.length) {
            if (a[i] == t) return i;
            i++;
        }
    }
    public static void main(String[] args) {
        System.out.println(find(new int[]{1, 2}, 2));
    }
}
