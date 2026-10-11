import java.util.*;
public class Main {
    public static void main(String[] args) {
        Map<Integer, String> m = new HashMap<>();
        int[] keys = {5, 100, -1, 17, 33, 1, 65536, 1000000, -100, 16, 32, 48, Integer.MAX_VALUE, Integer.MIN_VALUE, 0, 12345678};
        for (int k : keys) m.put(k, "v" + k);
        System.out.println(m);
        System.out.println(m.keySet());
        Set<Integer> hs = new HashSet<>();
        for (int i = 0; i < 40; i += 3) hs.add(i * i - 20);
        System.out.println(hs);
        Set<Integer> s2 = new HashSet<>();
        for (int i = 100; i > 0; i -= 7) s2.add(i);
        System.out.println(s2);
        Map<Integer, Integer> grow = new HashMap<>();
        for (int i = 0; i < 13; i++) grow.put(i * 16, i);
        System.out.println("13 multiples of 16: " + grow);
        for (int i = 13; i < 26; i++) grow.put(i * 16, i);
        System.out.println("26 multiples of 16: " + grow);
        Map<Integer, Integer> big = new HashMap<>();
        for (int i = 0; i < 100; i++) big.put((i * 37) % 211 - 50, i);
        System.out.println(big);
        Set<Integer> neg = new HashSet<>(Arrays.asList(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5));
        System.out.println(neg);
        neg.removeIf(x -> x < 0);
        System.out.println(neg);
        Map<Integer, Integer> cap = new HashMap<>(64);
        for (int i : new int[]{70, 6, 134, 5, 69}) cap.put(i, i);
        System.out.println("cap64 " + cap);
        Map<Integer, Integer> cap2 = new HashMap<>(2);
        for (int i : new int[]{70, 6, 134, 5, 69}) cap2.put(i, i);
        System.out.println("cap2 " + cap2);
    }
}
