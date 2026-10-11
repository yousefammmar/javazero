import java.util.*;
public class Main {
    public static void main(String[] args) {
        String text = "It was the best of times, it was the worst of times, it was the age of wisdom, it was the age of foolishness, "
            + "it was the epoch of belief, it was the epoch of incredulity, it was the season of Light, it was the season of Darkness";
        String[] words = text.toLowerCase().replaceAll("[^a-z ]", "").split("\\s+");
        System.out.println(words.length + " words");
        Map<String, Integer> freq = new HashMap<>();
        for (String w : words) freq.put(w, freq.getOrDefault(w, 0) + 1);
        System.out.println(freq);
        Map<String, Integer> sorted = new TreeMap<>(freq);
        System.out.println(sorted);
        List<Map.Entry<String, Integer>> es = new ArrayList<>(freq.entrySet());
        es.sort((a, b) -> b.getValue() != a.getValue().intValue() ? b.getValue() - a.getValue() : a.getKey().compareTo(b.getKey()));
        for (int i = 0; i < 5; i++) System.out.println((i + 1) + ". " + es.get(i).getKey() + " x" + es.get(i).getValue());
        Map<Integer, List<String>> byCount = new TreeMap<>(Comparator.reverseOrder());
        for (Map.Entry<String, Integer> e : sorted.entrySet()) byCount.computeIfAbsent(e.getValue(), k -> new ArrayList<>()).add(e.getKey());
        System.out.println(byCount);
        Map<Character, Integer> letters = new TreeMap<>();
        for (char c : text.toLowerCase().toCharArray()) if (Character.isLetter(c)) letters.merge(c, 1, Integer::sum);
        System.out.println(letters);
        Map<Character, Integer> hl = new HashMap<>(letters);
        System.out.println(hl);
        Set<String> unique = new HashSet<>(Arrays.asList(words));
        System.out.println(unique.size() + " unique: " + unique);
        Set<String> seen = new HashSet<>(), dup = new LinkedHashSet<>();
        for (String w : words) if (!seen.add(w)) dup.add(w);
        System.out.println("dups " + dup);
        Map<Integer, Integer> lens = new HashMap<>();
        for (String w : words) lens.merge(w.length(), 1, Integer::sum);
        System.out.println(lens);
        String longest = "";
        for (String w : words) if (w.length() > longest.length()) longest = w;
        System.out.println(longest);
    }
}
