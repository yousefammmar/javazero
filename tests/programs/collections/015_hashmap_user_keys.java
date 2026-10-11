import java.util.*;
public class Main {
    static final class Point {
        final int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return x == p.x && y == p.y;
        }
        @Override public int hashCode() { return 31 * x + y; }
        @Override public String toString() { return "(" + x + "," + y + ")"; }
    }
    static class BadKey {
        String id;
        BadKey(String id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof BadKey && ((BadKey) o).id.equals(id); }
        @Override public int hashCode() { return id.length(); }
        @Override public String toString() { return "BK:" + id; }
    }
    static class NoEquals {
        int v;
        NoEquals(int v) { this.v = v; }
        @Override public int hashCode() { return v; }
        @Override public String toString() { return "NE" + v; }
    }
    public static void main(String[] args) {
        Set<Point> ps = new HashSet<>();
        ps.add(new Point(1, 2)); ps.add(new Point(1, 2)); ps.add(new Point(2, 1)); ps.add(new Point(0, 0)); ps.add(new Point(5, -3));
        System.out.println(ps + " size " + ps.size());
        System.out.println(ps.contains(new Point(2, 1)) + " " + ps.contains(new Point(9, 9)));
        Map<Point, String> grid = new HashMap<>();
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) grid.put(new Point(i, j), i + "" + j);
        System.out.println(grid);
        System.out.println(grid.get(new Point(3, 2)));
        Set<BadKey> bad = new HashSet<>();
        for (String s : new String[]{"aa", "bb", "cc", "a", "b", "aa", "ddd"}) bad.add(new BadKey(s));
        System.out.println(bad + " " + bad.size());
        Set<NoEquals> ne = new HashSet<>();
        NoEquals one = new NoEquals(1);
        ne.add(one); ne.add(new NoEquals(1)); ne.add(new NoEquals(17)); ne.add(one);
        System.out.println(ne.size() + " " + ne.contains(one) + " " + ne.contains(new NoEquals(1)));
        Map<Point, Integer> counts = new HashMap<>();
        int[][] moves = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {0, 1}, {1, 0}, {0, 1}};
        int x = 0, y = 0;
        counts.merge(new Point(x, y), 1, Integer::sum);
        for (int[] mv : moves) { x += mv[0]; y += mv[1]; counts.merge(new Point(x, y), 1, Integer::sum); }
        System.out.println(counts);
        Point mut = new Point(7, 7);
        System.out.println(mut.equals(null) + " " + mut.equals("x") + " " + mut.hashCode());
    }
}
