import java.util.*;
import java.util.stream.*;

public class Main {
    record Sale(String region, String product, int units, double price) {
        double revenue() { return units * price; }
    }

    public static void main(String[] args) {
        List<Sale> sales = List.of(
            new Sale("North", "Widget", 10, 2.5), new Sale("North", "Gadget", 3, 10.0),
            new Sale("South", "Widget", 25, 2.4), new Sale("South", "Doohickey", 7, 7.75),
            new Sale("East", "Gadget", 12, 9.5), new Sale("East", "Widget", 8, 2.6),
            new Sale("West", "Doohickey", 1, 8.0), new Sale("West", "Gadget", 20, 9.9),
            new Sale("North", "Doohickey", 5, 7.5), new Sale("South", "Gadget", 9, 10.25));

        double total = sales.stream().mapToDouble(Sale::revenue).sum();
        System.out.printf("Total revenue: %.2f%n", total);
        System.out.println("Total units: " + sales.stream().mapToInt(Sale::units).sum());

        Map<String, Double> byRegion = sales.stream()
            .collect(Collectors.groupingBy(Sale::region, TreeMap::new, Collectors.summingDouble(Sale::revenue)));
        byRegion.forEach((k, v) -> System.out.printf("  %-6s %8.2f%n", k, v));

        Map<String, Long> countByProduct = sales.stream()
            .collect(Collectors.groupingBy(Sale::product, TreeMap::new, Collectors.counting()));
        System.out.println(countByProduct);

        System.out.println("Top 3 sales: " + sales.stream()
            .sorted(Comparator.comparingDouble(Sale::revenue).reversed())
            .limit(3).map(s -> s.region() + "/" + s.product()).collect(Collectors.toList()));

        System.out.println("Products: " + sales.stream().map(Sale::product).distinct().sorted().collect(Collectors.joining(", ", "[", "]")));
        Optional<Sale> cheapest = sales.stream().min(Comparator.comparingDouble(Sale::price));
        System.out.println("Cheapest: " + cheapest.map(Sale::toString).orElse("none"));
        System.out.println("Any > 20 units? " + sales.stream().anyMatch(s -> s.units() > 20)
            + ", all > 0? " + sales.stream().allMatch(s -> s.units() > 0)
            + ", none West-Widget? " + sales.stream().noneMatch(s -> s.region().equals("West") && s.product().equals("Widget")));

        DoubleSummaryStatistics st = sales.stream().mapToDouble(Sale::revenue).summaryStatistics();
        System.out.printf("min=%.2f max=%.2f avg=%.3f n=%d%n", st.getMin(), st.getMax(), st.getAverage(), st.getCount());

        Map<Boolean, List<String>> parts = sales.stream()
            .collect(Collectors.partitioningBy(s -> s.revenue() >= 50, Collectors.mapping(s -> s.region().charAt(0) + s.product().substring(0, 2), Collectors.toList())));
        System.out.println(parts);

        Map<String, Optional<Sale>> best = sales.stream()
            .collect(Collectors.groupingBy(Sale::product, TreeMap::new, Collectors.maxBy(Comparator.comparingInt(Sale::units))));
        best.forEach((p, s) -> System.out.println(p + " best seller: " + s.get().region() + " " + s.get().units()));

        System.out.println(IntStream.rangeClosed(1, 10).filter(i -> i % 2 == 1).boxed().collect(Collectors.toList()));
        System.out.println(IntStream.range(0, 5).mapToObj(i -> "n" + i).collect(Collectors.joining("|")));
        System.out.println(Stream.iterate(1, x -> x * 3).limit(8).reduce(0, Integer::sum));
        System.out.println(Arrays.stream(new int[]{5, 3, 8}).map(x -> x * x).max().getAsInt());
        System.out.println(Stream.of("b", "a", "c").sorted(Comparator.reverseOrder()).toList());
    }
}
