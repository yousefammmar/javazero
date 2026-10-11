import java.util.*;
public class Main {
    public static void main(String[] args) {
        String[] docs = {"the cat sat on the mat", "the dog sat on the log", "cats and dogs", "a cat and a dog", "the end"};
        Map<String, Set<Integer>> index = new HashMap<>();
        for (int i = 0; i < docs.length; i++)
            for (String w : docs[i].split(" ")) index.computeIfAbsent(w, k -> new TreeSet<>()).add(i);
        System.out.println(index);
        System.out.println(new TreeMap<>(index));
        String[] queries = {"cat", "dog", "the", "zebra"};
        for (String q : queries) System.out.println(q + " -> " + index.getOrDefault(q, Collections.emptySet()));
        Set<Integer> both = new TreeSet<>(index.get("cat"));
        both.retainAll(index.get("dog"));
        System.out.println("cat AND dog: " + both);
        Set<Integer> either = new TreeSet<>(index.get("cat"));
        either.addAll(index.get("dog"));
        System.out.println("cat OR dog: " + either);
        String[] anag = {"listen", "silent", "enlist", "google", "gogole", "cat", "act", "tac", "dog"};
        Map<String, List<String>> groups = new HashMap<>();
        for (String w : anag) {
            char[] cs = w.toCharArray();
            Arrays.sort(cs);
            groups.computeIfAbsent(new String(cs), k -> new ArrayList<>()).add(w);
        }
        System.out.println(groups);
        List<List<String>> out = new ArrayList<>(groups.values());
        out.sort((a, b) -> b.size() - a.size() != 0 ? b.size() - a.size() : a.get(0).compareTo(b.get(0)));
        System.out.println(out);
        int[] nums = {1, 15, 22, 7, 36, 41, 8, 29, 50, 13, 64};
        Map<Integer, List<Integer>> byDecade = new HashMap<>();
        for (int n : nums) byDecade.computeIfAbsent(n / 10, k -> new ArrayList<>()).add(n);
        System.out.println(byDecade);
        Map<Boolean, List<Integer>> parity = new HashMap<>();
        for (int n : nums) parity.computeIfAbsent(n % 2 == 0, k -> new ArrayList<>()).add(n);
        System.out.println(parity);
        Map<String, Integer> totals = new LinkedHashMap<>();
        String[][] sales = {{"east", "10"}, {"west", "5"}, {"east", "7"}, {"north", "3"}, {"west", "9"}};
        for (String[] s : sales) totals.merge(s[0], Integer.parseInt(s[1]), Integer::sum);
        System.out.println(totals);
    }
}
