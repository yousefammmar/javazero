import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("How many? ");
        int n = sc.nextInt();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            System.out.print("Number " + (i + 1) + ": ");
            sum += sc.nextInt();
        }
        System.out.println();
        System.out.println("Sum = " + sum);
        System.out.println("Average = " + (double) sum / n);
        System.out.printf("Avg2 = %.2f%n", (double) sum / n);
    }
}
