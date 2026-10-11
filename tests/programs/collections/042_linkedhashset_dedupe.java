import java.util.*;
import java.util.stream.*;
public class Main {
    public static void main(String[] args) {
        List<String> words = List.of("pear", "apple", "pear", "fig", "apple", "kiwi", "fig", "plum");
        Set<String> lhs = new LinkedHashSet<>(words);
        System.out.println(lhs + " " + lhs.size());
        System.out.println(new HashSet<>(words));
        System.out.println(new TreeSet<>(words));
        System.out.println(words.stream().distinct().collect(Collectors.toList()));
        lhs.add("pear"); lhs.add("zzz");
        lhs.remove("apple"); lhs.add("apple");
        System.out.println(lhs);
        Iterator<String> it = lhs.iterator();
        while (it.hasNext()) if (it.next().length() == 3) it.remove();
        System.out.println(lhs);
        List<Integer> nums = Arrays.asList(5, 3, 5, 9, 3, 1, 9, 7, 1);
        System.out.println(new ArrayList<>(new LinkedHashSet<>(nums)));
        Set<Integer> a = new LinkedHashSet<>(List.of(1, 2, 3, 4, 5, 6)), b = new LinkedHashSet<>(List.of(9, 6, 3, 0));
        Set<Integer> u = new LinkedHashSet<>(a); u.addAll(b);
        Set<Integer> i = new LinkedHashSet<>(a); i.retainAll(b);
        Set<Integer> d = new LinkedHashSet<>(b); d.removeAll(a);
        System.out.println(u + " " + i + " " + d);
        System.out.println(lhs.equals(new HashSet<>(lhs)) + " " + new LinkedHashSet<>(List.of(1, 2)).equals(new LinkedHashSet<>(List.of(2, 1))));
        Set<String> chars = new LinkedHashSet<>();
        for (String s : "mississippi".split("")) chars.add(s);
        System.out.println(chars + " " + String.join("", chars));
        Set<Character> cs = new LinkedHashSet<>();
        for (char c : "programming".toCharArray()) cs.add(c);
        System.out.println(cs);
        Set<Character> hc = new HashSet<>(cs);
        System.out.println(hc);
        Map<String, Integer> lhm = new LinkedHashMap<>();
        for (String w : words) lhm.merge(w, 1, Integer::sum);
        System.out.println(lhm);
        lhm.entrySet().removeIf(e -> e.getValue() == 1);
        System.out.println(lhm);
        List<String> l2 = new ArrayList<>(lhs);
        Collections.reverse(l2);
        System.out.println(l2 + " first " + lhs.iterator().next());
        System.out.println(lhs.stream().map(String::toUpperCase).collect(Collectors.joining("|")));
        Set<List<Integer>> sl = new LinkedHashSet<>();
        sl.add(List.of(1, 2)); sl.add(Arrays.asList(1, 2)); sl.add(List.of(2, 1));
        System.out.println(sl);
    }
}
