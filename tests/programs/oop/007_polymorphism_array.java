import java.util.*;

public class Main {
    static abstract class Shape {
        abstract double area();
        String name() { return getClass().getSimpleName(); }
        public String toString() { return name() + String.format("[area=%.2f]", area()); }
    }
    static class Circle extends Shape {
        double r; Circle(double r) { this.r = r; }
        double area() { return Math.PI * r * r; }
    }
    static class Rect extends Shape {
        double w, h; Rect(double w, double h) { this.w = w; this.h = h; }
        double area() { return w * h; }
    }
    static class Square extends Rect {
        Square(double s) { super(s, s); }
        @Override String name() { return "Sq"; }
    }
    static class Tri extends Shape {
        double b, h; Tri(double b, double h) { this.b = b; this.h = h; }
        double area() { return 0.5 * b * h; }
    }
    static String classify(Shape s) { return "shape"; }
    static String classify(Rect s) { return "rect"; }
    static String classify(Square s) { return "square"; }
    public static void main(String[] args) {
        Shape[] shapes = { new Circle(1), new Rect(2, 3), new Square(2), new Tri(3, 4) };
        double total = 0;
        for (Shape s : shapes) { System.out.println(s); total += s.area(); }
        System.out.printf("total=%.3f%n", total);
        for (Shape s : shapes) System.out.println(s.name() + " -> " + classify(s));
        Rect r = new Square(3);
        System.out.println(classify(r) + " " + classify((Shape) r) + " " + classify(new Square(1)));
        List<Shape> list = new ArrayList<>(Arrays.asList(shapes));
        list.sort(Comparator.comparingDouble(Shape::area));
        System.out.println(list);
        list.sort(Comparator.comparing(Shape::name).thenComparing(Shape::area, Comparator.reverseOrder()));
        System.out.println(list);
        Object[] objs = { shapes[0], "str", 3, 4.5, 'c', null };
        for (Object o : objs) {
            String d = o instanceof Shape ? "shape" : o instanceof Number ? "number" : o == null ? "null" : "other";
            System.out.println(d);
        }
    }
}
