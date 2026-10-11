import java.util.*;
public class Main {
    public static void main(String[] args) {
        Random r = new Random(2024);
        for (int i = 0; i < 3; i++) System.out.println(r.nextDouble());
        for (int i = 0; i < 8; i++) System.out.print(r.nextBoolean() + " ");
        System.out.println();
        for (int i = 0; i < 3; i++) System.out.println(r.nextLong());
        for (int i = 0; i < 4; i++) System.out.println(r.nextGaussian());
        for (int i = 0; i < 3; i++) System.out.println(r.nextFloat());
        byte[] b = new byte[6];
        r.nextBytes(b);
        System.out.println(Arrays.toString(b));
        Random r2 = new Random(99);
        r2.setSeed(5);
        System.out.println(r2.nextInt(50) + " " + r2.nextInt(50));
    }
}
