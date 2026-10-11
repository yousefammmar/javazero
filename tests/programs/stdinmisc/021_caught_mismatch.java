import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ok = 0;
        try {
            while (true) {
                int v = sc.nextInt();
                ok += v;
                System.out.println("read " + v);
            }
        } catch (InputMismatchException e) {
            System.out.println("Mismatch: " + e.getMessage());
        }
        System.out.println("ok=" + ok);
        System.out.println("next token still: " + sc.next());
    }
}
