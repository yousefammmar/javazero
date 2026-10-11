import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("pear", "apple", "fig", "apple", "kiwi", "pear", "plum");
        Set<String> set = new HashSet<>(list);
        System.out.println(list + " -> " + set + " size " + set.size());
        System.out.println("add dup: " + set.add("fig") + ", add new: " + set.add("lime"));
        System.out.println(set);
        Set<String> a = new HashSet<>(List.of("a", "b", "c", "d", "e"));
        Set<String> b = new HashSet<>(List.of("d", "e", "f", "g"));
        Set<String> union = new HashSet<>(a); union.addAll(b);
        Set<String> inter = new HashSet<>(a); inter.retainAll(b);
        Set<String> diff = new HashSet<>(a); diff.removeAll(b);
        Set<String> sym = new HashSet<>(union); sym.removeAll(inter);
        System.out.println("union " + union);
        System.out.println("inter " + inter);
        System.out.println("diff " + diff);
        System.out.println("sym " + sym);
        System.out.println("containsAll " + union.containsAll(a) + " " + a.containsAll(b));
        System.out.println("addAll changed? " + a.addAll(List.of("a", "b")) + " " + a.addAll(List.of("z")));
        System.out.println("removeAll changed? " + a.removeAll(List.of("q")) + " " + a.removeAll(List.of("z")));
        Set<Integer> n1 = new HashSet<>(); Set<Integer> n2 = new HashSet<>();
        for (int i = 0; i < 20; i++) { if (i % 2 == 0) n1.add(i); if (i % 3 == 0) n2.add(i); }
        System.out.println(n1 + " " + n2);
        n1.retainAll(n2);
        System.out.println(n1);
        System.out.println(new HashSet<>(List.of(1, 2, 3)).equals(new HashSet<>(List.of(3, 2, 1))));
        System.out.println(new HashSet<>(List.of(1, 2, 3)).hashCode());
        Iterator<String> it = set.iterator();
        while (it.hasNext()) if (it.next().length() == 4) it.remove();
        System.out.println(set);
        List<Integer> dups = List.of(5, 3, 5, 1, 3, 9, 1, 100, 200, 100);
        System.out.println(new HashSet<>(dups));
        System.out.println(new ArrayList<>(new LinkedHashSet<>(dups)));
        Object[] arr = set.toArray();
        System.out.println(arr.length);
        set.clear();
        System.out.println(set + " " + set.isEmpty());
    }
}
