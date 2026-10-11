import java.util.*;
import java.util.function.*;

public class Main {
    @FunctionalInterface interface TriFunc<A, B, C, R> { R apply(A a, B b, C c); }
    @FunctionalInterface interface IntOp { int apply(int a, int b); default IntOp then(IntOp o) { return (a, b) -> o.apply(apply(a, b), b); } }
    static int applyTwice(IntOp op, int a, int b) { return op.apply(op.apply(a, b), b); }
    static <T, R> List<R> map(List<T> in, Function<? super T, ? extends R> f) {
        List<R> out = new ArrayList<>();
        for (T t : in) out.add(f.apply(t));
        return out;
    }
    static class Acc { int total; void add(int x) { total += x; } static int sq(int x) { return x * x; } int plus(int x) { return total + x; } }
    public static void main(String[] args) {
        IntOp add = (a, b) -> a + b;
        IntOp mul = (a, b) -> a * b;
        IntOp sub = Math::subtractExact;
        System.out.println(add.apply(3, 4) + " " + mul.apply(3, 4) + " " + sub.apply(3, 4));
        System.out.println(applyTwice(add, 1, 10) + " " + applyTwice(mul, 2, 3));
        System.out.println(add.then(mul).apply(2, 5));
        TriFunc<Integer, Integer, Integer, Integer> t3 = (a, b, c) -> a * b + c;
        System.out.println(t3.apply(2, 3, 4));
        Function<Integer, Integer> inc = x -> x + 1, dbl = x -> x * 2;
        System.out.println(inc.andThen(dbl).apply(5) + " " + inc.compose(dbl).apply(5));
        Supplier<String> sup = () -> "sup";
        BiFunction<String, Integer, String> rep = String::repeat;
        UnaryOperator<String> up = String::toUpperCase;
        Predicate<String> empty = String::isEmpty;
        System.out.println(sup.get() + " " + rep.apply("ab", 3) + " " + up.apply("x") + " " + empty.negate().test(""));
        Predicate<Integer> even = x -> x % 2 == 0, big = x -> x > 10;
        System.out.println(even.and(big).test(12) + " " + even.or(big).test(11) + " " + even.and(big).test(4));
        Acc acc = new Acc();
        IntConsumer cons = acc::add;
        for (int i = 1; i <= 4; i++) cons.accept(i);
        IntUnaryOperator sq = Acc::sq;
        IntUnaryOperator pl = acc::plus;
        System.out.println(acc.total + " " + sq.applyAsInt(7) + " " + pl.applyAsInt(5));
        System.out.println(map(List.of(1, 2, 3), x -> "n" + x));
        System.out.println(map(List.of("a", "bb"), String::length));
        Supplier<Acc> mk = Acc::new;
        System.out.println(mk.get().total);
        Function<Integer, int[]> mkArr = int[]::new;
        System.out.println(mkArr.apply(3).length);
        BinaryOperator<Integer> mx = BinaryOperator.maxBy(Comparator.naturalOrder());
        System.out.println(mx.apply(3, 9));
        Runnable r = () -> System.out.println("run!");
        r.run();
    }
}
