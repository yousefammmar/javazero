import java.util.*;
public class Main {
    interface Shape { double area(); }
    record Circle(double r) implements Shape { public double area() { return Math.PI * r * r; } }
    enum Color { RED, GREEN }
    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(2));
        for (Shape s : shapes) System.out.printf("%.3f %s%n", s.area(), s);
        Map<String,Integer> m = new HashMap<>();
        for (String w : "the quick brown fox jumps over the lazy dog the end".split(" ")) m.merge(w, 1, Integer::sum);
        System.out.println(m);
        long big = 1L << 40; int x = Integer.MAX_VALUE; x++;
        System.out.println(big + " " + x + " " + 7 / 2.0 + " " + (char)('a' + 2) + " " + Color.valueOf("GREEN").ordinal());
        int[] a = {5, 3, 1}; Arrays.sort(a); System.out.println(Arrays.toString(a));
        String s = null;
        System.out.println(s.length());
    }
}
