import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> names = new ArrayList<>();
        while (sc.hasNextLine()) {
            String s = sc.nextLine().trim();
            if (s.isEmpty()) continue;
            names.add(s);
        }
        Collections.sort(names);
        System.out.println(names);
        names.sort(String.CASE_INSENSITIVE_ORDER);
        System.out.println(names);
        names.sort(Comparator.comparing(String::length).thenComparing(Comparator.reverseOrder()));
        System.out.println(names);
        Set<String> uniq = new LinkedHashSet<>(names);
        System.out.println(uniq.size() + " unique");
        for (String n : names) System.out.println(n.charAt(0) + "." + n.substring(1).length());
    }
}
