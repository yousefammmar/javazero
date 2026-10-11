import java.util.*;
public class Main {
    interface Shape { double area(); default String describe() { return getClass().getSimpleName() + String.format("[%.2f]", area()); } }
    record Circle(double r) implements Shape { public double area() { return Math.PI * r * r; } }
    record Rect(double w, double h) implements Shape { public double area() { return w * h; } }
    sealed interface Expr permits Num, Add, Mul {}
    record Num(int v) implements Expr {}
    record Add(Expr l, Expr r) implements Expr {}
    record Mul(Expr l, Expr r) implements Expr {}
    static int eval(Expr e) {
        return switch (e) {
            case Num n -> n.v();
            case Add a -> eval(a.l()) + eval(a.r());
            case Mul m -> eval(m.l()) * eval(m.r());
        };
    }
    static String classify(Object o) {
        return switch (o) {
            case null -> "null";
            case Integer i when i > 10 -> "big int " + i;
            case Integer i -> "int " + i;
            case String s -> "string of " + s.length();
            case int[] a -> "int array " + a.length;
            default -> "other " + o;
        };
    }
    public static void main(String[] args) {
        List<Shape> shapes = List.of(new Circle(1.5), new Rect(2, 3.5), new Circle(0));
        for (Shape s : shapes) System.out.println(s.describe() + " " + s);
        System.out.println(eval(new Add(new Num(2), new Mul(new Num(3), new Num(4)))));
        for (Object o : new Object[]{5, 50, "hey", new int[3], 2.5, null}) System.out.println(classify(o));
        int day = 3;
        String name = switch (day) { case 1, 7 -> "weekend"; case 2, 3, 4, 5, 6 -> { String t = "week"; yield t + "day"; } default -> "?"; };
        System.out.println(name);
        var list = new ArrayList<Map<String, List<Integer>>>();
        list.add(new TreeMap<>(Map.of("b", List.of(1), "a", List.of(2, 3))));
        System.out.println(list);
        Object o = "pattern";
        if (o instanceof String s && s.length() > 3) System.out.println(s.toUpperCase());
        if (!(o instanceof Integer n)) System.out.println("not int");
        StringBuilder sb = new StringBuilder();
        for (char c = 'a'; c <= 'e'; c++) sb.append(c).append(c - 'a').append(Character.toUpperCase(c));
        System.out.println(sb);
        System.out.println(String.format("%-10s|%10s|", "left", "right") + String.format("%3d|%-3d|%03d", 5, 5, 5));
        long count = "mississippi".chars().filter(c -> c == 's').count();
        System.out.println(count);
        System.out.println(Integer.toString(Integer.MAX_VALUE).length() + " " + ("" + 1.0f + 2L).length());
        int[][] tri = new int[5][];
        for (int i = 0; i < 5; i++) { tri[i] = new int[i + 1]; tri[i][0] = tri[i][i] = 1; for (int j = 1; j < i; j++) tri[i][j] = tri[i - 1][j - 1] + tri[i - 1][j]; }
        System.out.println(Arrays.deepToString(tri));
    }
}
