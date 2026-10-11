import java.util.function.*;
public class Main {
    public static void main(String[] args) {
        Function<Integer, Integer> dbl = x -> x * 2, inc = x -> x + 1;
        System.out.println(dbl.andThen(inc).apply(5) + " " + dbl.compose(inc).apply(5) + " " + Function.<Integer>identity().apply(9));
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println(add.andThen(dbl).apply(3, 4));
        Supplier<String> sup = () -> "supplied";
        System.out.println(sup.get());
        Predicate<String> empty = String::isEmpty, longer = s -> s.length() > 3;
        System.out.println(empty.negate().test("") + " " + empty.or(longer).test("hello") + " " + empty.negate().and(longer).test("abc") + " " + Predicate.not(empty).test("x") + " " + Predicate.<String>isEqual("a").test("a"));
        UnaryOperator<String> up = String::toUpperCase;
        BinaryOperator<Integer> mx = BinaryOperator.maxBy(Integer::compare);
        System.out.println(up.apply("abc") + " " + mx.apply(3, 8) + " " + BinaryOperator.<Integer>minBy(Integer::compare).apply(3, 8));
        Consumer<StringBuilder> c1 = sb -> sb.append("a"), c2 = sb -> sb.append("b");
        StringBuilder sb = new StringBuilder();
        c1.andThen(c2).andThen(c1).accept(sb);
        System.out.println(sb);
        IntBinaryOperator ib = (a, b) -> a * b; IntPredicate ev = n -> n % 2 == 0; ToIntFunction<String> len = String::length; IntFunction<String> stars = "*"::repeat; IntUnaryOperator neg = n -> -n; BooleanSupplier bs = () -> true; DoubleUnaryOperator half = d -> d / 2;
        System.out.println(ib.applyAsInt(6, 7) + " " + ev.test(3) + " " + len.applyAsInt("four") + " " + stars.apply(4) + " " + neg.andThen(n -> n * 10).applyAsInt(3) + " " + bs.getAsBoolean() + " " + half.applyAsDouble(5));
        // four kinds of method references
        Function<String, Integer> parse = Integer::parseInt;
        Supplier<java.util.ArrayList<String>> ctor = java.util.ArrayList::new;
        String prefix = "pre-";
        Function<String, String> bound = prefix::concat;
        BiFunction<String, String, Boolean> unbound = String::equalsIgnoreCase;
        Function<Integer, int[]> arrCtor = int[]::new;
        System.out.println(parse.apply("12") + 1 + " " + ctor.get() + " " + bound.apply("x") + " " + unbound.apply("A", "a") + " " + arrCtor.apply(3).length);
        // closures
        int[] counter = {0};
        Supplier<Integer> next = () -> ++counter[0];
        next.get(); next.get();
        System.out.println(next.get() + " " + counter[0]);
        Supplier<Supplier<Integer>> mk = () -> { int[] c = {10}; return () -> c[0]++; };
        Supplier<Integer> s1 = mk.get(), s2 = mk.get();
        System.out.println(s1.get() + " " + s1.get() + " " + s2.get());
        // recursion via lambdas
        Function<Integer, Integer>[] fact = new Function[1];
        fact[0] = n -> n <= 1 ? 1 : n * fact[0].apply(n - 1);
        System.out.println(fact[0].apply(10));
        BiFunction<BiFunction, Integer, Integer> fib = (self, n) -> n < 2 ? n : (Integer) self.apply(self, n - 1) + (Integer) self.apply(self, n - 2);
        System.out.println(fib.apply(fib, 15));
        Function<Integer, Function<Integer, Integer>> curried = a -> b -> a - b;
        System.out.println(curried.apply(10).apply(3));
        Runnable r = () -> System.out.println("run " + prefix);
        r.run();
        Comparable<String> cmp = "m"::compareTo;
        System.out.println(cmp.compareTo("a"));
    }
}
