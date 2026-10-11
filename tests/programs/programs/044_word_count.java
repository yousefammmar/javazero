import java.util.*;

public class Main {
    static final String TEXT = """
        It was the best of times, it was the worst of times, it was the age of wisdom,
        it was the age of foolishness, it was the epoch of belief, it was the epoch of
        incredulity, it was the season of Light, it was the season of Darkness, it was
        the spring of hope, it was the winter of despair.
        """;

    public static void main(String[] args) {
        String[] words = TEXT.toLowerCase().split("[^a-z]+");
        Map<String, Integer> freq = new HashMap<>();
        int total = 0;
        for (String w : words) {
            if (w.isEmpty()) continue;
            freq.put(w, freq.getOrDefault(w, 0) + 1);
            total++;
        }
        System.out.println("Total words: " + total + ", unique: " + freq.size());
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(freq.entrySet());
        entries.sort((a, b) -> b.getValue() != a.getValue().intValue() ? b.getValue() - a.getValue() : a.getKey().compareTo(b.getKey()));
        System.out.println("Top 8:");
        for (int i = 0; i < 8; i++) {
            Map.Entry<String, Integer> e = entries.get(i);
            System.out.printf("  %-12s %2d %s%n", e.getKey(), e.getValue(), "#".repeat(e.getValue()));
        }
        String longest = "";
        for (String w : freq.keySet()) if (w.length() > longest.length() || (w.length() == longest.length() && w.compareTo(longest) < 0)) longest = w;
        System.out.println("Longest word: " + longest + " (" + longest.length() + ")");
        Map<Integer, TreeSet<String>> byLen = new TreeMap<>();
        for (String w : freq.keySet()) byLen.computeIfAbsent(w.length(), k -> new TreeSet<>()).add(w);
        for (Map.Entry<Integer, TreeSet<String>> e : byLen.entrySet()) System.out.println(e.getKey() + ": " + e.getValue());
        String[] lines = TEXT.split("\n");
        System.out.println("Lines: " + lines.length);
        for (int i = 0; i < lines.length; i++) System.out.println((i + 1) + ": " + lines[i].length() + " chars, " + lines[i].trim().split("\\s+").length + " words");
        int letters = 0, upper = 0, punct = 0;
        for (char c : TEXT.toCharArray()) {
            if (Character.isLetter(c)) { letters++; if (Character.isUpperCase(c)) upper++; }
            else if (",.".indexOf(c) >= 0) punct++;
        }
        System.out.println("letters=" + letters + " upper=" + upper + " punct=" + punct);
        System.out.println(TEXT.indexOf("worst") + " " + TEXT.lastIndexOf("it was") + " " + TEXT.contains("Darkness") + " " + TEXT.charAt(3));
    }
}
