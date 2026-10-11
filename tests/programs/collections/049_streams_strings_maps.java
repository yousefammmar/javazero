import java.util.*;
import java.util.function.*;
import java.util.stream.*;
public class Main {
    public static void main(String[] args) {
        String text = "the rain in spain stays mainly in the plain and the train runs in the rain";
        List<String> ws = Arrays.asList(text.split(" "));
        Map<String, Long> freq = ws.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(freq);
        System.out.println(new TreeMap<>(freq));
        System.out.println(freq.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey())).limit(4).map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", ")));
        System.out.println(ws.stream().collect(Collectors.groupingBy(String::length, Collectors.toSet())));
        TreeMap<Integer, List<String>> byLen = ws.stream().collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList()));
        System.out.println(byLen);
        System.out.println(ws.stream().filter(w -> w.contains("ain")).distinct().sorted().collect(Collectors.toList()));
        System.out.println(ws.stream().map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1)).collect(Collectors.joining(" ")));
        System.out.println(ws.stream().mapToInt(String::length).summaryStatistics());
        System.out.println(ws.stream().collect(Collectors.toMap(w -> w, String::length, (x, y) -> x)));
        System.out.println(ws.stream().collect(Collectors.partitioningBy(w -> w.length() > 3, Collectors.mapping(w -> w.substring(0, 1), Collectors.toSet()))));
        System.out.println(text.chars().filter(Character::isLetter).mapToObj(c -> (char) c).collect(Collectors.groupingBy(c -> c, Collectors.counting())));
        System.out.println(text.chars().filter(c -> "aeiou".indexOf(c) >= 0).count());
        System.out.println(Arrays.stream(text.split(" ")).map(StringBuilder::new).map(StringBuilder::reverse).map(StringBuilder::toString).limit(5).collect(Collectors.toList()));
        System.out.println(Stream.of("a,b", "c", "d,e,f").flatMap(s -> Arrays.stream(s.split(","))).collect(Collectors.toList()));
        System.out.println(ws.stream().anyMatch(w -> w.equals("spain")) + " " + ws.stream().allMatch(w -> w.length() < 8) + " " + ws.stream().noneMatch(String::isEmpty));
        System.out.println(ws.stream().reduce("", (acc, w) -> acc.length() >= w.length() ? acc : w));
        System.out.println(ws.stream().sorted(Comparator.comparing(String::length).thenComparing(Comparator.reverseOrder())).distinct().skip(2).limit(5).toList());
        System.out.println(IntStream.range(0, ws.size()).filter(i -> ws.get(i).equals("in")).boxed().toList());
        System.out.println(ws.stream().collect(Collectors.groupingBy(w -> w.charAt(0), Collectors.collectingAndThen(Collectors.toList(), List::size))));
        Map<Boolean, Long> pc = ws.stream().collect(Collectors.partitioningBy(w -> w.startsWith("t"), Collectors.counting()));
        System.out.println(pc);
        TreeSet<Integer> lens = ws.stream().map(String::length).collect(Collectors.toCollection(TreeSet::new));
        System.out.println(lens);
        System.out.println(String.join("-", new TreeSet<>(ws)));
    }
}
