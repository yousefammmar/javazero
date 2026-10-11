import java.util.*;

public class Main {
    public static void main(String[] args) {
        Random rnd = new Random(42);
        int rolls = 600;
        int[] counts = new int[13];
        int[] single = new int[7];
        int doubles = 0, longestStreak = 0, streak = 0, lastTotal = -1;
        for (int i = 0; i < rolls; i++) {
            int a = rnd.nextInt(6) + 1, b = rnd.nextInt(6) + 1;
            counts[a + b]++;
            single[a]++;
            if (a == b) doubles++;
            if (a + b == 7 && lastTotal == 7) streak++;
            else streak = a + b == 7 ? 1 : 0;
            longestStreak = Math.max(longestStreak, streak);
            lastTotal = a + b;
        }
        System.out.println("Two-dice totals over " + rolls + " rolls:");
        for (int s = 2; s <= 12; s++) {
            double expected = rolls * (6 - Math.abs(s - 7)) / 36.0;
            System.out.printf("%2d | %-30s %3d (expected %5.1f)%n", s, "*".repeat(counts[s] / 3), counts[s], expected);
        }
        System.out.println("Doubles: " + doubles + " (" + String.format("%.1f", 100.0 * doubles / rolls) + "%)");
        System.out.println("Longest run of consecutive sevens: " + longestStreak);
        System.out.println("First die faces: " + Arrays.toString(Arrays.copyOfRange(single, 1, 7)));
        int mode = 2;
        for (int s = 3; s <= 12; s++) if (counts[s] > counts[mode]) mode = s;
        System.out.println("Most common total: " + mode);
        Random r2 = new Random(42);
        System.out.println(r2.nextInt(100) + " " + r2.nextInt(100) + " " + r2.nextInt(100));
        System.out.println(r2.nextLong() + " " + r2.nextBoolean());
        System.out.printf("%.6f%n", r2.nextDouble());
        List<Integer> deck = new ArrayList<>();
        for (int i = 1; i <= 10; i++) deck.add(i);
        Collections.shuffle(deck, new Random(7));
        System.out.println("shuffled: " + deck);
        System.out.println(new Random(1).nextGaussian());
    }
}
