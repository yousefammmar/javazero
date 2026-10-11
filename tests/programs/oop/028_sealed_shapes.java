import java.util.*;

public class Main {
    sealed interface Shape permits Circle, Rect, Tri {}
    record Circle(double r) implements Shape {}
    record Rect(double w, double h) implements Shape {}
    record Tri(double b, double h) implements Shape {}
    static double area(Shape s) {
        return switch (s) {
            case Circle c -> Math.PI * c.r() * c.r();
            case Rect r -> r.w() * r.h();
            case Tri t -> 0.5 * t.b() * t.h();
        };
    }
    static String name(Shape s) {
        return switch (s) {
            case Circle c when c.r() > 10 -> "big circle";
            case Circle c -> "circle";
            case Rect r when r.w() == r.h() -> "square";
            case Rect r -> "rect";
            case Tri t -> "tri";
        };
    }
    static String objDesc(Object o) {
        return switch (o) {
            case null -> "null!";
            case Integer i when i > 100 -> "large int " + i;
            case Integer i -> "int " + i;
            case String s when s.isEmpty() -> "empty string";
            case String s -> "string " + s.length();
            case Shape sh -> "shape " + name(sh);
            case int[] arr -> "int array " + arr.length;
            default -> "other " + o;
        };
    }
    public static void main(String[] args) {
        List<Shape> shapes = List.of(new Circle(1), new Circle(11), new Rect(2, 3), new Rect(4, 4), new Tri(3, 5));
        double total = 0;
        for (Shape s : shapes) {
            System.out.printf("%s %s %.2f%n", s, name(s), area(s));
            total += area(s);
        }
        System.out.printf("total %.3f%n", total);
        Object[] objs = { 5, 500, "", "hello", new Circle(2), new int[3], 2.5, null, 'c' };
        for (Object o : objs) System.out.println(objDesc(o));
        System.out.println(new Circle(1).equals(new Circle(1.0)) + " " + new Rect(1, 2).equals(new Rect(2, 1)));
        Shape sh = new Tri(1, 1);
        if (sh instanceof Tri(double b, double h) && b == h) System.out.println("isosceles right " + b);
    }
}
