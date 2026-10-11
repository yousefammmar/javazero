import java.util.*;
public class Main {
    public static void main(String[] args) {
        Random r = new Random(42);
        for (int i = 0; i < 5; i++) System.out.print(r.nextInt() + " ");
        System.out.println();
        r = new Random(12345L);
        for (int i = 0; i < 6; i++) System.out.print(r.nextInt(10) + " ");
        System.out.println();
        for (int i = 0; i < 6; i++) System.out.print(r.nextInt(16) + " ");
        System.out.println();
        for (int i = 0; i < 6; i++) System.out.print(r.nextInt(100) + " ");
        System.out.println();
        for (int i = 0; i < 4; i++) System.out.print(r.nextInt(1 << 20) + " ");
        System.out.println();
        for (int i = 0; i < 4; i++) System.out.print(r.nextInt(1000003) + " ");
        System.out.println();
        System.out.println(new Random(0).nextInt(1));
        System.out.println(new Random(-7).nextInt(7));
    }
}
