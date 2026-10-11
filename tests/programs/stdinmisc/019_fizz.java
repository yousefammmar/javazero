import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt(), b = sc.nextInt();
        int total = 0;
        for (int i = a; i <= b; i++) {
            String s = i % 15 == 0 ? "FizzBuzz" : i % 5 == 0 ? "Buzz" : i % 3 == 0 ? "Fizz" : String.valueOf(i);
            System.out.print(s + (i < b ? "," : "\n"));
            total += i;
        }
        System.out.println("total " + total);
        System.out.println(a / b + " " + a % b + " " + b / a);
    }
}
