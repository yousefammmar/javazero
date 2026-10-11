import java.util.*;
import java.util.stream.*;
public class Main {
    static Optional<Integer> parse(String s) {
        try { return Optional.of(Integer.parseInt(s)); } catch (NumberFormatException e) { return Optional.empty(); }
    }
    @SafeVarargs
    static <T> List<T> listOf(T... xs) { return new ArrayList<>(Arrays.asList(xs)); }
    static int sum(int... xs) { int s = 0; for (int x : xs) s += x; return s; }
    static String desc(String label, Object... xs) { return label + xs.length + (xs.length > 0 ? Arrays.toString(xs) : ""); }
    static <T extends Comparable<T>> T maxOf(T first, T... rest) { T m = first; for (T t : rest) if (t.compareTo(m) > 0) m = t; return m; }
    public static void main(String[] args) {
        System.out.println(parse("42").map(x -> x * 2).filter(x -> x > 50).orElse(-1));
        System.out.println(parse("abc").map(x -> x * 2).orElse(-1) + " " + parse("x").isPresent() + " " + parse("7").isEmpty());
        System.out.println(parse("5").flatMap(x -> parse("" + (x + 1))).get() + " " + parse("q").or(() -> Optional.of(0)).get() + " " + parse("3"));
        System.out.println(Optional.empty() + " " + Optional.ofNullable(null).orElseGet(() -> "gen") + " " + Optional.of("v").map(String::toUpperCase).get());
        parse("9").ifPresent(v -> System.out.println("present " + v));
        parse("z").ifPresentOrElse(v -> System.out.println("no"), () -> System.out.println("absent"));
        try { Optional.empty().get(); } catch (NoSuchElementException e) { System.out.println(e.getMessage()); }
        try { Optional.empty().orElseThrow(() -> new IllegalStateException("none")); } catch (IllegalStateException e) { System.out.println(e.getMessage()); }
        try { Optional.of(null); } catch (NullPointerException e) { System.out.println("NPE"); }
        System.out.println(listOf(1, 2, 3) + " " + listOf() + " " + listOf("a") + " " + sum() + " " + sum(1) + " " + sum(1, 2, 3) + " " + sum(new int[]{4, 5}));
        System.out.println(desc("n") + " " + desc("n", 1, "a") + " " + desc("n", (Object[]) new String[]{"x", "y"}) + " " + desc("n", (Object) new String[]{"x", "y"}).length());
        System.out.println(maxOf(3, 9, 2) + " " + maxOf("pear", "apple") + " " + maxOf(1.5));
        System.out.println(Stream.of("a", "bb", "ccc").map(String::length).collect(Collectors.toList()) + " " + IntStream.rangeClosed(1, 5).sum() + " " + Stream.of(1, 2, 3, 4).filter(x -> x % 2 == 0).findFirst() + " " + IntStream.range(0, 5).boxed().collect(Collectors.toList()) + " " + Stream.of("x", "y").collect(Collectors.joining(",", "[", "]")));
        System.out.println(IntStream.of(3, 1, 2).max() + " " + IntStream.empty().max() + " " + IntStream.of(1, 2).average() + " " + Stream.of(5, 3).min(Comparator.naturalOrder()).get() + " " + OptionalInt.of(3) + " " + OptionalDouble.empty());
    }
}
