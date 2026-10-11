import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Start: "); int start = sc.nextInt();
        System.out.print("End: "); int end = sc.nextInt();
        System.out.print("Step: "); int step = sc.nextInt();
        System.out.println("  C      F");
        for (int c = start; c <= end; c += step) {
            double f = c * 9.0 / 5 + 32;
            System.out.printf("%4d %8.2f%n", c, f);
        }
        System.out.println(100 * 9 / 5 + 32);
        System.out.println(-40 * 9.0 / 5 + 32);
    }
}
