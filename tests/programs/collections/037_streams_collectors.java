import java.util.*;
import java.util.function.*;
import java.util.stream.*;
public class Main {
    record Emp(String name, String dept, int salary, int age) {}
    public static void main(String[] args) {
        List<Emp> es = List.of(new Emp("Ann", "eng", 120, 30), new Emp("Bob", "eng", 100, 45), new Emp("Cy", "ops", 80, 28),
            new Emp("Dee", "ops", 90, 39), new Emp("Eli", "hr", 70, 51), new Emp("Fay", "eng", 130, 35), new Emp("Gus", "hr", 75, 29));
        System.out.println(es.stream().map(Emp::name).collect(Collectors.joining(", ", "[", "]")));
        Map<String, List<String>> g = es.stream().collect(Collectors.groupingBy(Emp::dept, Collectors.mapping(Emp::name, Collectors.toList())));
        System.out.println(g);
        System.out.println(new TreeMap<>(g));
        TreeMap<String, Long> tmc = es.stream().collect(Collectors.groupingBy(Emp::dept, TreeMap::new, Collectors.counting()));
        System.out.println(tmc);
        System.out.println(es.stream().collect(Collectors.groupingBy(Emp::dept, Collectors.counting())));
        System.out.println(es.stream().collect(Collectors.groupingBy(Emp::dept, Collectors.summingInt(Emp::salary))));
        System.out.println(es.stream().collect(Collectors.groupingBy(Emp::dept, Collectors.averagingDouble(Emp::salary))));
        System.out.println(es.stream().collect(Collectors.groupingBy(e -> e.age() / 10 * 10, Collectors.mapping(Emp::name, Collectors.joining("/")))));
        System.out.println(es.stream().collect(Collectors.partitioningBy(e -> e.salary() >= 90)));
        System.out.println(es.stream().collect(Collectors.partitioningBy(e -> e.age() > 35, Collectors.counting())));
        Map<String, Integer> toMap = es.stream().collect(Collectors.toMap(Emp::name, Emp::salary));
        System.out.println(toMap);
        System.out.println(es.stream().collect(Collectors.toMap(Emp::dept, Emp::salary, Integer::sum)));
        LinkedHashMap<String, String> lm = es.stream().collect(Collectors.toMap(Emp::dept, Emp::name, (a, b) -> a + "+" + b, LinkedHashMap::new));
        System.out.println(lm);
        try { es.stream().collect(Collectors.toMap(Emp::dept, Emp::name)); } catch (IllegalStateException e) { System.out.println("ISE " + e.getMessage()); }
        System.out.println(es.stream().collect(Collectors.summingInt(Emp::salary)) + " " + es.stream().collect(Collectors.averagingInt(Emp::age)));
        System.out.println(es.stream().collect(Collectors.groupingBy(Emp::dept, Collectors.maxBy(Comparator.comparingInt(Emp::salary)))).get("eng").get().name());
        System.out.println(es.stream().map(Emp::dept).collect(Collectors.toSet()));
        TreeSet<String> depts = es.stream().map(Emp::dept).collect(Collectors.toCollection(TreeSet::new));
        System.out.println(depts);
        System.out.println(es.stream().collect(Collectors.counting()));
        IntSummaryStatistics s = es.stream().collect(Collectors.summarizingInt(Emp::salary));
        System.out.println(s.getMax() + " " + s.getMin() + " " + s.getAverage());
        System.out.println(Stream.of("apple", "avocado", "banana", "blueberry", "cherry").collect(Collectors.groupingBy(w -> w.charAt(0))));
        System.out.println(Stream.of("one", "two", "three", "four").collect(Collectors.groupingBy(String::length)));
        System.out.println(Stream.of(1, 2, 3, 4, 5, 6).collect(Collectors.partitioningBy(x -> x % 2 == 0, Collectors.summingInt(x -> x))));
        System.out.println(es.stream().sorted(Comparator.comparingInt(Emp::salary).reversed()).limit(3).map(Emp::name).toList());
        List<String> cat = Stream.of("b", "a", "c").collect(Collectors.collectingAndThen(Collectors.toList(), l -> { Collections.sort(l); return l; }));
        System.out.println(cat);
        System.out.println(es.stream().collect(Collectors.groupingBy(Emp::dept, Collectors.reducing(0, Emp::age, Integer::max))));
    }
}
