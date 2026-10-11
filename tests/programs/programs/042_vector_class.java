import java.util.*;

public class Main {
    static final class Vec {
        final double x, y, z;
        Vec(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }

        Vec plus(Vec o) { return new Vec(x + o.x, y + o.y, z + o.z); }
        Vec minus(Vec o) { return new Vec(x - o.x, y - o.y, z - o.z); }
        Vec times(double k) { return new Vec(x * k, y * k, z * k); }
        double dot(Vec o) { return x * o.x + y * o.y + z * o.z; }
        Vec cross(Vec o) { return new Vec(y * o.z - z * o.y, z * o.x - x * o.z, x * o.y - y * o.x); }
        double length() { return Math.sqrt(dot(this)); }
        Vec normalized() { double l = length(); return new Vec(x / l, y / l, z / l); }
        double angleDeg(Vec o) { return Math.toDegrees(Math.acos(dot(o) / (length() * o.length()))); }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Vec)) return false;
            Vec v = (Vec) o;
            return Double.compare(x, v.x) == 0 && Double.compare(y, v.y) == 0 && Double.compare(z, v.z) == 0;
        }
        @Override public int hashCode() { return Objects.hash(x, y, z); }
        @Override public String toString() { return String.format("(%.2f, %.2f, %.2f)", x, y, z); }
    }

    static class Matrix2 {
        final double a, b, c, d;
        Matrix2(double a, double b, double c, double d) { this.a = a; this.b = b; this.c = c; this.d = d; }
        Matrix2 times(Matrix2 o) {
            return new Matrix2(a * o.a + b * o.c, a * o.b + b * o.d, c * o.a + d * o.c, c * o.b + d * o.d);
        }
        double det() { return a * d - b * c; }
        Matrix2 inverse() {
            double k = 1 / det();
            return new Matrix2(d * k, -b * k, -c * k, a * k);
        }
        @Override public boolean equals(Object o) {
            return o instanceof Matrix2 m && m.a == a && m.b == b && m.c == c && m.d == d;
        }
        @Override public int hashCode() { return Objects.hash(a, b, c, d); }
        @Override public String toString() { return "[" + a + " " + b + "; " + c + " " + d + "]"; }
    }

    public static void main(String[] args) {
        Vec a = new Vec(1, 2, 3), b = new Vec(4, -5, 6);
        System.out.println("a=" + a + " b=" + b);
        System.out.println("a+b=" + a.plus(b) + " a-b=" + a.minus(b) + " 2a=" + a.times(2));
        System.out.println("dot=" + a.dot(b) + " cross=" + a.cross(b));
        System.out.printf("|a|=%.4f unit=%s angle=%.3f%n", a.length(), a.normalized(), a.angleDeg(b));
        Vec i = new Vec(1, 0, 0), j = new Vec(0, 1, 0);
        System.out.println("i x j = " + i.cross(j) + ", angle(i,j)=" + i.angleDeg(j));
        System.out.println(a.equals(new Vec(1, 2, 3)) + " " + a.equals(b) + " " + (a.hashCode() == new Vec(1, 2, 3).hashCode()));
        Set<Vec> set = new HashSet<>(List.of(a, b));
        System.out.println(set.contains(new Vec(4, -5, 6)) + " " + set.contains(new Vec(0, 0, 0)));
        System.out.println(new Vec(0.0, 0, 0).equals(new Vec(-0.0, 0, 0)));
        Matrix2 m = new Matrix2(2, 1, 5, 3);
        System.out.println(m + " det=" + m.det() + " inv=" + m.inverse());
        System.out.println(m.times(m.inverse()) + " " + m.times(m.inverse()).equals(new Matrix2(1, 0, 0, 1)));
        Vec acc = new Vec(0, 0, 0);
        for (int k = 1; k <= 5; k++) acc = acc.plus(new Vec(k, k * k, 1.0 / k));
        System.out.println("acc=" + acc);
        Map<Vec, String> names = new HashMap<>();
        names.put(i, "i"); names.put(j, "j");
        System.out.println(names.get(new Vec(0, 1, 0)));
    }
}
