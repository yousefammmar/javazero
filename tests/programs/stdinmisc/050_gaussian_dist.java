import java.util.*;
public class Main {
    public static void main(String[] args) {
        Random r = new Random(7);
        double sum = 0, sq = 0;
        int n = 1000;
        for (int i = 0; i < n; i++) { double g = r.nextGaussian(); sum += g; sq += g * g; }
        System.out.println(sum + " " + sq);
        System.out.printf("%.6f %.6f%n", sum / n, Math.sqrt(sq / n - (sum / n) * (sum / n)));
        Random q = new Random(100);
        int inside = 0;
        for (int i = 0; i < 10000; i++) { double x = q.nextDouble(), y = q.nextDouble(); if (x * x + y * y <= 1) inside++; }
        System.out.println(inside + " " + 4.0 * inside / 10000);
        Random s = new Random(3);
        long acc = 0;
        for (int i = 0; i < 1000; i++) acc += s.nextInt(1 << 10) + s.nextInt(37) * 3L + (s.nextBoolean() ? 1 : 0) + (long) (s.nextDouble() * 100) + (s.nextLong() >>> 60);
        System.out.println(acc);
        Random t = new Random(-1L);
        System.out.println(t.nextInt() + " " + t.nextLong() + " " + t.nextInt(Integer.MAX_VALUE) + " " + t.nextInt(5, 10) + " " + t.nextDouble());
        Random u = new Random(0);
        int[] hist = new int[10];
        for (int i = 0; i < 5000; i++) hist[u.nextInt(10)]++;
        System.out.println(Arrays.toString(hist));
        StringBuilder sb = new StringBuilder();
        Random v = new Random(2);
        for (int i = 0; i < 20; i++) sb.append((char) ('a' + v.nextInt(26)));
        System.out.println(sb);
        List<Integer> deck = new ArrayList<>();
        for (int i = 0; i < 52; i++) deck.add(i);
        Collections.shuffle(deck, new Random(2024));
        System.out.println(deck.subList(0, 5) + " " + deck.get(51));
    }
}
