import java.util.*;
public class Main {
    public static void main(String[] args) {
        Map<Character, Integer> cm = new HashMap<>();
        for (char c : "the quick brown fox jumps over the lazy dog".toCharArray()) cm.merge(c, 1, Integer::sum);
        System.out.println(cm);
        Map<Double, String> dm = new HashMap<>();
        double[] ds = {1.5, 0.0, -0.0, 2.25, 3.14159, 100.0, -7.5, 1e10, 0.1, 0.2, 0.30000000000000004};
        for (double d : ds) dm.put(d, "d" + d);
        System.out.println(dm);
        Map<Long, Integer> lm = new HashMap<>();
        long[] ls = {1L, 10000000000L, -1L, 123456789012345L, Long.MAX_VALUE, Long.MIN_VALUE, 4294967296L, 42L};
        for (long l : ls) lm.put(l, (int) (l % 100));
        System.out.println(lm);
        Map<Boolean, String> bm = new HashMap<>();
        bm.put(true, "yes"); bm.put(false, "no");
        System.out.println(bm);
        Set<Boolean> bs = new HashSet<>(List.of(true, false));
        System.out.println(bs);
        Set<Character> chars = new HashSet<>();
        for (char c = 'z'; c >= 'a'; c -= 3) chars.add(c);
        System.out.println(chars);
        Set<Character> up = new HashSet<>();
        for (char c : "Hello, World! 123".toCharArray()) up.add(c);
        System.out.println(up);
        Map<Short, Byte> sb = new HashMap<>();
        for (short s = -3; s < 4; s++) sb.put((short) (s * 1000), (byte) s);
        System.out.println(sb);
        Map<Float, Integer> fm = new HashMap<>();
        fm.put(1.5f, 1); fm.put(2.5f, 2); fm.put(-0.5f, 3); fm.put(100f, 4);
        System.out.println(fm);
        System.out.println(Double.valueOf(1.5).hashCode() + " " + Long.valueOf(10000000000L).hashCode() + " " + Boolean.TRUE.hashCode() + " " + Character.valueOf('a').hashCode());
        System.out.println("hello".hashCode() + " " + "".hashCode() + " " + "The quick brown fox".hashCode());
    }
}
