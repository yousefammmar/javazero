import java.util.*;
import java.util.stream.*;
public class Main {
    record Person(String name, int age, String city) {}
    public static void main(String[] args) {
        List<Person> ps = new ArrayList<>(List.of(
            new Person("Zoe", 30, "Paris"), new Person("Adam", 25, "Rome"), new Person("Bob", 30, "Oslo"),
            new Person("Cara", 25, "Paris"), new Person("Dan", 41, "Rome"), new Person("Eve", 30, "Oslo")));
        ps.sort(Comparator.comparing(Person::name));
        System.out.println("by name " + names(ps));
        ps.sort(Comparator.comparingInt(Person::age));
        System.out.println("by age (stable) " + names(ps));
        ps.sort(Comparator.comparingInt(Person::age).reversed());
        System.out.println("by age desc " + names(ps));
        ps.sort(Comparator.comparing(Person::city).thenComparing(Person::age).thenComparing(Person::name, Comparator.reverseOrder()));
        System.out.println("city, age, name desc " + names(ps));
        ps.sort(Comparator.comparing(Person::city, Comparator.reverseOrder()).thenComparingInt(Person::age));
        System.out.println("city desc, age " + names(ps));
        List<String> withNull = new ArrayList<>(Arrays.asList("b", null, "a", null, "c"));
        withNull.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println(withNull);
        withNull.sort(Comparator.nullsLast(Comparator.reverseOrder()));
        System.out.println(withNull);
        List<Integer> li = new ArrayList<>(List.of(3, 1, 2));
        li.sort(Comparator.naturalOrder());
        System.out.println(li);
        li.sort(Comparator.<Integer>naturalOrder().reversed());
        System.out.println(li);
        Comparator<String> byLen = (a, b) -> a.length() - b.length();
        List<String> ws = Arrays.asList("ccc", "a", "bb", "dd", "e");
        ws.sort(byLen);
        System.out.println(ws);
        ws.sort(byLen.thenComparing(Comparator.reverseOrder()));
        System.out.println(ws);
        System.out.println(Collections.max(ws, byLen) + " " + Collections.min(ws, byLen));
        System.out.println(ps.stream().max(Comparator.comparingInt(Person::age)).get());
        System.out.println(ps.stream().min(Comparator.comparing(Person::name)).get().name());
        System.out.println("compare: " + "a".compareTo("b") + " " + "apple".compareTo("apricot") + " " + Integer.compare(5, 3) + " " + Boolean.compare(true, false) + " " + Character.compare('a', 'd') + " " + Double.compare(0.0, -0.0));
        System.out.println(ps.get(0));
    }
    static String names(List<Person> l) { return l.stream().map(Person::name).collect(Collectors.joining(",")); }
}
