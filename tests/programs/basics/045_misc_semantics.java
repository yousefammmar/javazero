import java.util.ArrayList;
import java.util.List;

public class Main {
    static int sf = init("static field");
    static int init(String s) { System.out.println("init " + s); return 1; }
    static String describe(Object o) {
        if (o == null) return "null";
        if (o instanceof Integer i && i > 10) return "big int " + i;
        if (o instanceof Integer i) return "int " + i;
        if (o instanceof String s) return "string len " + s.length();
        if (o instanceof int[] arr) return "int[] len " + arr.length;
        if (o instanceof Number n) return "number " + n.intValue();
        return "other " + o;
    }
    static String sw(Object o) {
        return switch (o) {
            case Integer i when i < 0 -> "neg";
            case Integer i -> "int" + i;
            case String s -> "str" + s;
            case null -> "nul";
            default -> "dflt";
        };
    }
    interface Shape { double area(); default String label() { return getClass().getSimpleName() + String.format("(%.2f)", area()); } }
    record Circle(double r) implements Shape { public double area() { return Math.PI * r * r; } }
    record Rect(double w, double h) implements Shape { public double area() { return w * h; } }
    public static void main(String[] args) {
        System.out.println("main " + sf);
        for (Object o : new Object[]{null, 5, 50, "hey", new int[3], 2.9, 'c', List.of(1)}) System.out.println(describe(o));
        for (Object o : new Object[]{-1, 4, "s", null, 1.5}) System.out.println(sw(o));
        Shape[] shapes = {new Circle(1), new Rect(2, 3.5), new Circle(0.5)};
        double total = 0;
        for (Shape s : shapes) { System.out.println(s.label() + " " + s); total += s.area(); }
        System.out.printf("%.4f%n", total);
        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 10; i++) nums.add(i * i % 7);
        System.out.println(nums);
        nums.remove(Integer.valueOf(4));
        nums.remove(0);
        System.out.println(nums + " " + nums.indexOf(2) + " " + nums.contains(6) + " " + nums.size());
        nums.removeIf(n -> n % 2 == 0);
        System.out.println(nums);
        System.out.println(nums.stream().mapToInt(Integer::intValue).sum());
        java.util.Collections.sort(nums, java.util.Collections.reverseOrder());
        System.out.println(nums);
        StringBuilder sb = new StringBuilder();
        for (int i : new int[]{3, 1, 2}) sb.append(i).append(',');
        sb.setLength(sb.length() - 1);
        System.out.println(sb);
        Object o = 42;
        long l = (Integer) o;
        double d = (int) o;
        System.out.println(l + " " + d);
        Object arr = new int[]{1, 2};
        System.out.println(((int[]) arr).length + " " + (arr instanceof Object) + " " + (o instanceof Comparable));
        int x = 7;
        String parity = (x & 1) == 0 ? "even" : "odd";
        System.out.println(parity);
        char grade = 'B';
        String msg = switch (grade) { case 'A' -> "top"; case 'B', 'C' -> "ok"; default -> "low"; };
        System.out.println(msg);
        int[] counter = {0};
        Runnable r = () -> counter[0] += 5;
        r.run(); r.run();
        System.out.println(counter[0]);
        java.util.function.Function<Integer, Integer> sq = n -> n * n;
        java.util.function.BiFunction<Integer, Integer, Integer> add = (p, q) -> p + q;
        System.out.println(sq.apply(9) + " " + add.apply(2, 3) + " " + sq.andThen(n -> n + 1).apply(3));
        System.out.println(java.util.stream.IntStream.rangeClosed(1, 5).map(n -> n * n).boxed().toList());
        System.out.println(java.util.stream.Stream.of("b", "a", "c").sorted().reduce("", String::concat));
    }
}
