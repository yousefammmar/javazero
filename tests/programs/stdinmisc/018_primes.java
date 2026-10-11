import java.util.Scanner;
public class Main {
    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; (long) i * i <= n; i++) if (n % i == 0) return false;
        return true;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextInt()) {
            int n = sc.nextInt();
            if (n == 0) break;
            StringBuilder f = new StringBuilder();
            int m = Math.abs(n);
            for (int p = 2; p <= m; p++) while (m % p == 0) { f.append(f.length() > 0 ? "*" : "").append(p); m /= p; }
            System.out.println(n + (isPrime(n) ? " prime" : " composite/other") + " factors=" + f);
        }
        System.out.println("done, next token: " + (sc.hasNext() ? sc.next() : "-"));
    }
}
