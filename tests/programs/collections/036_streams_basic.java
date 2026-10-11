import java.util.*;
import java.util.stream.*;
public class Main {
    public static void main(String[] args) {
        List<Integer> nums = List.of(5, 3, 8, 1, 9, 2, 7, 3, 8, 6);
        System.out.println(nums.stream().filter(n -> n % 2 == 0).collect(Collectors.toList()));
        System.out.println(nums.stream().map(n -> n * n).collect(Collectors.toList()));
        System.out.println(nums.stream().mapToInt(Integer::intValue).sum());
        System.out.println(nums.stream().mapToInt(n -> n).average().getAsDouble());
        System.out.println(nums.stream().max(Integer::compare).get() + " " + nums.stream().min(Comparator.naturalOrder()).get());
        System.out.println(nums.stream().sorted().collect(Collectors.toList()));
        System.out.println(nums.stream().sorted(Comparator.reverseOrder()).limit(3).collect(Collectors.toList()));
        System.out.println(nums.stream().distinct().collect(Collectors.toList()));
        System.out.println(nums.stream().skip(7).collect(Collectors.toList()) + " " + nums.stream().skip(2).limit(3).collect(Collectors.toList()));
        System.out.println(nums.stream().reduce(0, Integer::sum) + " " + nums.stream().reduce(Integer::max).get() + " " + nums.stream().reduce(1, (a, b) -> a * b % 1000));
        System.out.println(nums.stream().count() + " " + nums.stream().distinct().count());
        System.out.println(nums.stream().anyMatch(n -> n > 8) + " " + nums.stream().allMatch(n -> n > 0) + " " + nums.stream().noneMatch(n -> n > 9));
        System.out.println(nums.stream().filter(n -> n > 6).findFirst().get() + " " + nums.stream().filter(n -> n > 100).findFirst().isPresent());
        System.out.println(IntStream.range(0, 5).boxed().collect(Collectors.toList()) + " " + IntStream.rangeClosed(1, 5).sum() + " " + IntStream.rangeClosed(1, 5).reduce(1, (a, b) -> a * b));
        System.out.println(Stream.iterate(1, x -> x * 2).limit(10).collect(Collectors.toList()));
        System.out.println(Stream.iterate(1, x -> x < 100, x -> x * 3).collect(Collectors.toList()));
        System.out.println(Stream.of("a", "bb", "ccc").map(String::length).collect(Collectors.toList()));
        System.out.println(Stream.of(List.of(1, 2), List.of(3), List.<Integer>of(), List.of(4, 5)).flatMap(List::stream).map(x -> x * 10).collect(Collectors.toList()));
        System.out.println("hello world".chars().filter(ch -> ch == 'o').count() + " " + "hello".chars().sum() + " " + "abc".chars().mapToObj(ch -> String.valueOf((char) ch)).collect(Collectors.joining("-")));
        System.out.println("banana".chars().distinct().mapToObj(ch -> "" + (char) ch).collect(Collectors.joining()));
        IntSummaryStatistics st = nums.stream().mapToInt(i -> i).summaryStatistics();
        System.out.println(st.getMin() + " " + st.getMax() + " " + st.getSum() + " " + st.getCount() + " " + st.getAverage());
        System.out.println(IntStream.of(3, 1, 2).sorted().boxed().collect(Collectors.toList()) + " " + IntStream.range(0, 20).filter(i -> i % 7 == 0).boxed().collect(Collectors.toList()));
        System.out.println(Arrays.stream(new int[]{4, 5, 6}).map(x -> x * 2).sum() + " " + DoubleStream.of(1.5, 2.5).sum() + " " + Stream.of(1, 2, 3, 4).mapToDouble(x -> x).average().orElse(0));
        List<String> peeked = new ArrayList<>();
        Stream.of("x", "y", "z").peek(peeked::add).map(String::toUpperCase).forEach(s -> peeked.add(s));
        System.out.println(peeked);
        System.out.println(IntStream.range(0, 5).mapToObj(i -> "i" + i).toList());
        System.out.println(Stream.of(5, 1, 4).sorted().findFirst().get() + " " + Stream.of(1, 2, 3, 4, 5).takeWhile(x -> x < 4).toList() + " " + Stream.of(1, 2, 3, 4, 5).dropWhile(x -> x < 4).toList());
    }
}
