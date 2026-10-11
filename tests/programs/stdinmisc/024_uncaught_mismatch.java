import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int a = sc.nextInt();
        System.out.println("Got " + a);
        System.out.print("Enter another: ");
        int b = sc.nextInt();
        System.out.println("Sum " + (a + b));
    }
}
