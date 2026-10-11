import java.util.*;

public class Main {
    interface Shape {
        double area();
        double perimeter();
        default String describe() {
            return String.format("%s[area=%.3f, perimeter=%.3f]", name(), area(), perimeter());
        }
        String name();
    }

    record Circle(double r) implements Shape {
        public double area() { return Math.PI * r * r; }
        public double perimeter() { return 2 * Math.PI * r; }
        public String name() { return "Circle"; }
    }

    record Rect(double w, double h) implements Shape {
        public double area() { return w * h; }
        public double perimeter() { return 2 * (w + h); }
        public String name() { return w == h ? "Square" : "Rect"; }
    }

    record Triangle(double a, double b, double c) implements Shape {
        Triangle {
            if (a + b <= c || a + c <= b || b + c <= a) throw new IllegalArgumentException("Impossible triangle " + a + "," + b + "," + c);
        }
        public double area() {
            double s = perimeter() / 2;
            return Math.sqrt(s * (s - a) * (s - b) * (s - c));
        }
        public double perimeter() { return a + b + c; }
        public String name() { return "Triangle"; }
    }

    static abstract class Polygon implements Shape {
        protected final double[][] pts;
        Polygon(double[][] pts) { this.pts = pts; }
        public double area() {
            double s = 0;
            for (int i = 0; i < pts.length; i++) {
                double[] p = pts[i], q = pts[(i + 1) % pts.length];
                s += p[0] * q[1] - q[0] * p[1];
            }
            return Math.abs(s) / 2;
        }
        public double perimeter() {
            double s = 0;
            for (int i = 0; i < pts.length; i++) {
                double[] p = pts[i], q = pts[(i + 1) % pts.length];
                s += Math.hypot(p[0] - q[0], p[1] - q[1]);
            }
            return s;
        }
    }

    static class Pentagon extends Polygon {
        Pentagon() { super(new double[][]{{0, 0}, {4, 0}, {5, 3}, {2, 5}, {-1, 3}}); }
        public String name() { return "Pentagon"; }
    }

    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<>(List.of(new Circle(1.5), new Rect(3, 4), new Rect(2, 2),
            new Triangle(3, 4, 5), new Triangle(7, 8, 9), new Pentagon()));
        for (Shape s : shapes) System.out.println(s.describe());
        try {
            shapes.add(new Triangle(1, 2, 10));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        shapes.sort(Comparator.comparingDouble(Shape::area));
        StringBuilder sb = new StringBuilder();
        for (Shape s : shapes) sb.append(s.name()).append(' ');
        System.out.println("By area: " + sb.toString().trim());
        double total = 0;
        for (Shape s : shapes) total += s.area();
        System.out.printf("Total area %.4f%n", total);
        Shape biggest = Collections.max(shapes, Comparator.comparingDouble(Shape::perimeter));
        System.out.println("Longest perimeter: " + biggest.name());
        System.out.println(new Circle(2).equals(new Circle(2.0)) + " " + new Rect(1, 2) + " " + new Circle(1).hashCode());
        for (Shape s : shapes) {
            String kind = switch (s) {
                case Circle c -> "round r=" + c.r();
                case Rect r -> "box " + r.w() + "x" + r.h();
                case Triangle t -> "three sides";
                default -> "other";
            };
            System.out.println(kind);
        }
    }
}
