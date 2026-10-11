import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE, count = 0;
        while (sc.hasNextInt()) {
            int v = sc.nextInt();
            if (v > max) max = v;
            if (v < min) min = v;
            count++;
        }
        System.out.println("count=" + count);
        System.out.println("max=" + max + " min=" + min);
        System.out.println("leftover=" + (sc.hasNext() ? sc.next() : "none"));
    }
}
