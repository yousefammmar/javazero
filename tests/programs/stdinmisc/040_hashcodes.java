import java.util.*;
public class Main {
    public static void main(String[] args) {
        String[] ss = {"", "a", "abc", "hello", "Hello, World!", "The quick brown fox", "zzzzzzzzzzzz", "polygenelubricants", "Aa", "BB", "日本語", "\n", "0123456789"};
        for (String s : ss) System.out.println("\"" + s.replace("\n", "\\n") + "\" " + s.hashCode());
        System.out.println(Integer.hashCode(42) + " " + Integer.valueOf(-1).hashCode() + " " + Long.hashCode(1L << 32) + " " + Long.valueOf(-5L).hashCode() + " " + Long.hashCode(Long.MAX_VALUE));
        System.out.println(Boolean.hashCode(true) + " " + Boolean.hashCode(false) + " " + Character.hashCode('a') + " " + Short.hashCode((short) -3) + " " + Byte.hashCode((byte) 7));
        System.out.println(Double.hashCode(1.0) + " " + Double.hashCode(0.0) + " " + Double.hashCode(-0.0) + " " + Double.hashCode(3.14) + " " + Double.hashCode(Double.NaN) + " " + Float.hashCode(1.5f) + " " + Double.valueOf(100).hashCode());
        System.out.println(Objects.hash(1, 2, 3) + " " + Objects.hash("a", "b") + " " + Objects.hash() + " " + Objects.hash((Object) null) + " " + Objects.hashCode(null) + " " + Objects.hash(1.5, 'c', true, 10L));
        System.out.println(Arrays.hashCode(new int[]{1, 2, 3}) + " " + Arrays.hashCode(new int[0]) + " " + Arrays.hashCode(new String[]{"x", null}) + " " + Arrays.hashCode(new double[]{1.5, 2.5}) + " " + Arrays.hashCode(new char[]{'a', 'b'}) + " " + Arrays.hashCode(new long[]{1L << 40}) + " " + Arrays.hashCode(new boolean[]{true, false}) + " " + Arrays.deepHashCode(new int[][]{{1}, {2, 3}}));
        System.out.println(List.of(1, 2, 3).hashCode() + " " + Arrays.asList("a", "b").hashCode() + " " + new ArrayList<>().hashCode() + " " + List.of().hashCode() + " " + new LinkedList<>(List.of(5)).hashCode());
        Map<String, Integer> m = new HashMap<>();
        m.put("one", 1); m.put("two", 2); m.put("three", 3);
        System.out.println(m.hashCode() + " " + new TreeMap<>(m).hashCode() + " " + Map.entry("k", 9).hashCode() + " " + new HashMap<>().hashCode());
        System.out.println(new HashSet<>(List.of(1, 2, 3)).hashCode() + " " + Set.of("a").hashCode() + " " + new TreeSet<>(List.of("x", "y")).hashCode());
        System.out.println(Optional.of(7).hashCode() + " " + Optional.empty().hashCode() + " " + Character.valueOf('z').hashCode() + " " + "ab".equals("ab") + " " + Objects.equals(null, null) + " " + Objects.equals("a", null) + " " + Objects.equals(1, 1L));
        System.out.println(new int[0].equals(new int[0]) + " " + Arrays.equals(new int[]{1}, new int[]{1}) + " " + Arrays.deepEquals(new int[][]{{1}}, new int[][]{{1}}));
        System.out.println(Objects.requireNonNullElse(null, "dflt") + " " + Objects.toString(null) + " " + Objects.toString(null, "z") + " " + Objects.isNull(null) + " " + Objects.compare("a", "b", Comparator.naturalOrder()));
        record P(int x, String y) {}
        System.out.println(new P(1, "a").equals(new P(1, "a")) + " " + new P(1, "a") + " " + (new P(1, "a").hashCode() == new P(1, "a").hashCode()));
    }
}
