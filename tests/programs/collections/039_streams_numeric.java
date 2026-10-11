import java.util.*;
import java.util.stream.*;
public class Main {
    static boolean isPrime(int n) { return n > 1 && IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(d -> n % d == 0); }
    public static void main(String[] args) {
        System.out.println(IntStream.rangeClosed(1, 50).filter(Main::isPrime).boxed().collect(Collectors.toList()));
        System.out.println(IntStream.rangeClosed(1, 10).mapToObj(i -> i + "^2=" + i * i).collect(Collectors.joining(" ")));
        System.out.println(Stream.iterate(new int[]{0, 1}, f -> new int[]{f[1], f[0] + f[1]}).limit(15).map(f -> f[0]).collect(Collectors.toList()));
        System.out.println(IntStream.rangeClosed(1, 20).filter(i -> i % 3 == 0 || i % 5 == 0).sum());
        System.out.println(LongStream.rangeClosed(1, 20).reduce(1L, (a, b) -> a * b));
        System.out.println(IntStream.range(0, 10).map(i -> 10 - i).boxed().sorted().map(String::valueOf).collect(Collectors.joining(",")));
        System.out.println(IntStream.iterate(1, i -> i * 2).limit(8).sum() + " " + IntStream.range(0, 0).sum() + " " + IntStream.range(5, 2).count());
        System.out.println(Stream.of("apple", "fig", "banana", "kiwi").mapToInt(String::length).max().getAsInt());
        System.out.println(Stream.of("apple", "fig", "banana", "kiwi").collect(Collectors.averagingInt(String::length)));
        System.out.println(Stream.of(1.5, 2.5, 3.0).mapToDouble(Double::doubleValue).sum() + " " + DoubleStream.of(1, 2, 3, 4).map(Math::sqrt).sum());
        System.out.println(IntStream.rangeClosed(1, 5).mapToObj(i -> "*".repeat(i)).collect(Collectors.toList()));
        List<int[]> pairs = IntStream.rangeClosed(1, 4).boxed().flatMap(a -> IntStream.rangeClosed(a, 4).mapToObj(b -> new int[]{a, b})).filter(p -> (p[0] + p[1]) % 3 == 0).collect(Collectors.toList());
        pairs.forEach(p -> System.out.print(Arrays.toString(p)));
        System.out.println();
        System.out.println(IntStream.rangeClosed(1, 100).filter(i -> Integer.toString(i).chars().map(c -> c - '0').sum() == 10).boxed().toList());
        int[] arr = {5, 2, 8, 2, 9, 5, 1};
        System.out.println(Arrays.stream(arr).boxed().sorted(Comparator.reverseOrder()).toList() + " " + Arrays.stream(arr).distinct().count() + " " + Arrays.toString(Arrays.stream(arr).sorted().toArray()));
        System.out.println(Arrays.stream(arr).boxed().collect(Collectors.groupingBy(i -> i, Collectors.counting())));
        System.out.println(Arrays.stream(new String[]{"b", "a", "c"}).sorted().collect(Collectors.joining()));
        System.out.println(IntStream.range(0, arr.length).filter(i -> arr[i] == 2).boxed().toList());
        System.out.println(IntStream.range(0, 5).mapToObj(Integer::toString).collect(Collectors.joining("+")) + "=" + IntStream.range(0, 5).sum());
        double[] da = Stream.of(1, 2, 3).mapToDouble(i -> i / 2.0).toArray();
        System.out.println(Arrays.toString(da));
        System.out.println(Stream.of(1, 2, 3, 4).map(i -> i * 1.5).reduce(0.0, Double::sum));
        System.out.println(IntStream.rangeClosed('a', 'e').mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining()));
        TreeMap<Character, Long> mis = "Mississippi".chars().boxed().collect(Collectors.groupingBy(c -> (char) c.intValue(), TreeMap::new, Collectors.counting()));
        System.out.println(mis);
        System.out.println(Stream.of(1, 2, 3, 4, 5).mapToInt(Integer::intValue).filter(i -> i > 2).average().orElse(-1));
        System.out.println(IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toMap(i -> i, i -> i * i)));
    }
}
