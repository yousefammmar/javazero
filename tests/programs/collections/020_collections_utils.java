import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(5, 3, 8, 1, 9, 2, 7, 3));
        Collections.sort(list);
        System.out.println("sort " + list);
        Collections.reverse(list);
        System.out.println("reverse " + list);
        Collections.shuffle(list, new Random(42));
        System.out.println("shuffle(42) " + list);
        List<String> w = new ArrayList<>(List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"));
        Collections.shuffle(w, new Random(7));
        System.out.println("shuffle(7) " + w);
        System.out.println("max " + Collections.max(list) + " min " + Collections.min(list));
        System.out.println("max by comparator " + Collections.max(w, Comparator.reverseOrder()));
        System.out.println("frequency " + Collections.frequency(list, 3) + " " + Collections.frequency(list, 100));
        Collections.swap(list, 0, list.size() - 1);
        System.out.println("swap " + list);
        Collections.sort(list);
        System.out.println("binarySearch 8: " + Collections.binarySearch(list, 8) + " 4: " + Collections.binarySearch(list, 4) + " 0: " + Collections.binarySearch(list, 0) + " 100: " + Collections.binarySearch(list, 100));
        List<Integer> un = Collections.unmodifiableList(list);
        try { un.add(1); } catch (UnsupportedOperationException e) { System.out.println("UOE unmodifiable"); }
        list.add(1000);
        System.out.println("view sees: " + un.size());
        System.out.println(Collections.nCopies(4, "ab") + " " + Collections.emptyList() + " " + Collections.singletonList(5) + " " + Collections.emptyMap() + " " + Collections.singleton("s"));
        try { Collections.emptyList().add(null); } catch (UnsupportedOperationException e) { System.out.println("UOE empty"); }
        list.sort(Collections.reverseOrder());
        System.out.println(list);
        Collections.rotate(list, 2);
        System.out.println("rotate 2 " + list);
        Collections.fill(w, "z");
        System.out.println(w);
        List<Integer> dst = new ArrayList<>(Collections.nCopies(3, 0));
        Collections.copy(dst, List.of(7, 8, 9));
        System.out.println(dst);
        Collections.addAll(dst, 1, 2);
        System.out.println(dst + " disjoint " + Collections.disjoint(dst, List.of(100)) + " " + Collections.disjoint(dst, List.of(9)));
        List<String> ws = new ArrayList<>(List.of("Banana", "apple", "cherry", "Apple"));
        Collections.sort(ws);
        System.out.println(ws);
        ws.sort(String.CASE_INSENSITIVE_ORDER);
        System.out.println(ws);
        System.out.println(Collections.indexOfSubList(dst, List.of(1, 2)));
        Collections.replaceAll(dst, 0, -5);
        System.out.println(dst);
    }
}
