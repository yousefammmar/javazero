import java.util.*;
public class Main {
    public static void main(String[] args) {
        Map<String, Integer> m = new HashMap<>();
        for (int i = 1; i <= 30; i++) {
            m.put("key" + i, i);
            if (i == 12 || i == 13 || i == 24 || i == 25 || i == 26) System.out.println("size " + i + ": " + m);
        }
        System.out.println(m.keySet());
        Set<String> hs = new HashSet<>();
        String[] fruit = {"apple", "banana", "cherry", "date", "elderberry", "fig", "grape", "honeydew", "kiwi", "lemon", "mango", "nectarine", "orange", "papaya", "quince"};
        for (String f : fruit) hs.add(f);
        System.out.println(hs);
        for (String f : hs) System.out.print(f.charAt(0));
        System.out.println();
        Map<String, Integer> big = new HashMap<>();
        for (int i = 0; i < 100; i++) big.put("item-" + (i * 7), i);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> e : big.entrySet()) sb.append(e.getValue()).append(',');
        System.out.println(sb);
        System.out.println("size " + big.size());
        for (int i = 0; i < 100; i += 2) big.remove("item-" + (i * 7));
        sb.setLength(0);
        for (int v : big.values()) sb.append(v).append(' ');
        System.out.println(sb);
        for (int i = 0; i < 100; i += 4) big.put("item-" + (i * 7), -i);
        sb.setLength(0);
        for (String k : big.keySet()) sb.append(k.substring(5)).append(' ');
        System.out.println(sb);
        Set<String> removed = new HashSet<>(hs);
        removed.remove("apple"); removed.remove("kiwi");
        removed.add("apple");
        System.out.println(removed);
    }
}
