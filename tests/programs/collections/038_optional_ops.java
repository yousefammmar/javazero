import java.util.*;
import java.util.stream.*;
public class Main {
    static Optional<Integer> parse(String s) {
        try { return Optional.of(Integer.parseInt(s)); } catch (NumberFormatException e) { return Optional.empty(); }
    }
    public static void main(String[] args) {
        Optional<String> present = Optional.of("hello"), empty = Optional.empty();
        System.out.println(present + " " + empty);
        System.out.println(present.isPresent() + " " + empty.isPresent() + " " + present.isEmpty() + " " + empty.isEmpty());
        System.out.println(present.get() + " " + empty.orElse("dflt") + " " + present.orElse("dflt") + " " + empty.orElseGet(() -> "gen"));
        System.out.println(present.map(String::length) + " " + empty.map(String::length) + " " + present.map(s -> (String) null));
        System.out.println(present.filter(s -> s.startsWith("h")) + " " + present.filter(s -> s.startsWith("x")));
        present.ifPresent(s -> System.out.println("ifPresent " + s));
        empty.ifPresent(s -> System.out.println("never"));
        present.ifPresentOrElse(s -> System.out.println("has " + s), () -> System.out.println("none"));
        empty.ifPresentOrElse(s -> System.out.println("has " + s), () -> System.out.println("none"));
        System.out.println(present.flatMap(s -> Optional.of(s + "!")) + " " + empty.or(() -> Optional.of("alt")));
        try { empty.get(); } catch (NoSuchElementException e) { System.out.println("NSEE " + e.getMessage()); }
        try { empty.orElseThrow(); } catch (NoSuchElementException e) { System.out.println("NSEE orElseThrow"); }
        try { empty.orElseThrow(() -> new IllegalStateException("custom")); } catch (IllegalStateException e) { System.out.println(e.getMessage()); }
        try { Optional.of(null); } catch (NullPointerException e) { System.out.println("NPE of(null)"); }
        System.out.println(Optional.ofNullable(null) + " " + Optional.ofNullable("x"));
        System.out.println(present.equals(Optional.of("hello")) + " " + present.hashCode() + " " + empty.hashCode());
        for (String s : new String[]{"12", "x", "-7", "3.5", ""}) System.out.println("'" + s + "' -> " + parse(s) + " " + parse(s).map(n -> n * 2).orElse(-1));
        System.out.println(Stream.of("1", "b", "3").map(Main::parse).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()));
        System.out.println(Stream.of("1", "b", "3").map(Main::parse).flatMap(Optional::stream).mapToInt(i -> i).sum());
        OptionalInt oi = IntStream.empty().max();
        System.out.println(oi + " " + oi.isPresent() + " " + IntStream.of(4, 9).max() + " " + oi.orElse(-1));
        OptionalDouble od = IntStream.of(1, 2).average();
        System.out.println(od + " " + od.getAsDouble() + " " + DoubleStream.empty().average());
        System.out.println(Stream.of(3, 1, 2).min(Integer::compare) + " " + Stream.<Integer>empty().findAny());
        Map<String, Integer> m = new HashMap<>(Map.of("k", 5));
        System.out.println(Optional.ofNullable(m.get("k")).map(v -> v + 1).orElse(0) + " " + Optional.ofNullable(m.get("z")).map(v -> v + 1).orElse(0));
    }
}
