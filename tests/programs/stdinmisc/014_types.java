import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        long l = sc.nextLong();
        double d = sc.nextDouble();
        boolean b = sc.nextBoolean();
        String w = sc.next();
        System.out.println(i + " " + l + " " + d + " " + b + " " + w);
        System.out.println(i * 2 + " " + (l + 1) + " " + d / 3 + " " + !b + " " + w.toUpperCase());
        double e = sc.nextDouble();
        System.out.println(e);
        String bs = sc.next();
        System.out.println(bs + " " + sc.nextBoolean());
        System.out.println(sc.hasNext());
    }
}
