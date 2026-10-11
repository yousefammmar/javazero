import java.util.*;
public class Main {
    public static void main(String[] args) {
        Map<String, Integer> m = new HashMap<>();
        System.out.println("put: " + m.put("apple", 1));
        System.out.println("put again: " + m.put("apple", 2));
        m.put("banana", 3); m.put("cherry", 4); m.put("date", 5); m.put("elderberry", 6);
        System.out.println(m);
        System.out.println("get missing: " + m.get("zzz"));
        System.out.println("getOrDefault: " + m.getOrDefault("zzz", -1) + " " + m.getOrDefault("date", -1));
        System.out.println("putIfAbsent existing: " + m.putIfAbsent("apple", 99) + ", new: " + m.putIfAbsent("fig", 7));
        System.out.println("containsKey " + m.containsKey("fig") + " containsValue " + m.containsValue(7) + " " + m.containsValue(8));
        System.out.println("remove: " + m.remove("fig") + " " + m.remove("fig"));
        System.out.println("remove(k,v) wrong: " + m.remove("apple", 1) + " right: " + m.remove("apple", 2));
        System.out.println(m + " size " + m.size());
        for (Map.Entry<String, Integer> e : m.entrySet()) System.out.println(e.getKey() + " -> " + e.getValue());
        System.out.println("keys " + m.keySet());
        System.out.println("values " + m.values());
        m.replaceAll((k, v) -> v * 10 + k.length());
        System.out.println(m);
        m.replace("banana", 0);
        System.out.println(m.replace("nope", 1) + " " + m);
        Map<String, Integer> other = new HashMap<>();
        other.put("cherry", 1); other.put("zebra", 26); other.put("yak", 25);
        m.putAll(other);
        System.out.println("putAll: " + m);
        Map<String, Integer> copy = new HashMap<>(m);
        System.out.println("equals copy: " + m.equals(copy) + " hash eq " + (m.hashCode() == copy.hashCode()));
        copy.put("extra", 0);
        System.out.println("equals after: " + m.equals(copy));
        m.keySet().remove("zebra");
        m.values().remove(25);
        m.entrySet().removeIf(e -> e.getKey().startsWith("b"));
        System.out.println(m);
        m.clear();
        System.out.println(m + " " + m.isEmpty());
        Map<String, String> nulls = new HashMap<>();
        nulls.put(null, "nullkey"); nulls.put("k", null);
        System.out.println(nulls + " " + nulls.get(null) + " " + nulls.containsKey("k") + " " + nulls.get("k"));
    }
}
