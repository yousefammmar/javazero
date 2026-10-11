import java.util.*;

public class Main {
    interface Limits {
        int MAX = 10;
        String UNIT = "kg";
        int[] TABLE = { 1, 2, 3 };
        double RATIO = MAX / 4;
        double RATIO2 = MAX / 4.0;
    }
    static class Impl implements Limits {
        int twice() { return MAX * 2; }
        String show() { return MAX + UNIT + RATIO + "/" + RATIO2; }
    }
    static class Base {
        final void fixed() { System.out.println("Base.fixed"); }
        void open() { System.out.println("Base.open"); }
        static final String NAME = "base";
        final int id;
        final List<String> items = new ArrayList<>();
        Base(int id) { this.id = id; }
    }
    static final class Leaf extends Base {
        Leaf() { super(77); }
        @Override void open() { System.out.println("Leaf.open then"); super.open(); }
    }
    public static void main(String[] args) {
        Impl i = new Impl();
        System.out.println(i.twice() + " " + i.show());
        System.out.println(Limits.MAX + Impl.MAX + " " + Limits.UNIT.length() + " " + Limits.TABLE.length);
        Limits.TABLE[0] = 100;
        System.out.println(Arrays.toString(Limits.TABLE));
        Leaf l = new Leaf();
        l.fixed(); l.open();
        System.out.println(l.id + " " + Base.NAME + " " + Leaf.NAME);
        l.items.add("a"); l.items.add("b");
        System.out.println(l.items);
        final int[] counter = { 0 };
        final int k;
        if (args.length > 5) k = 1; else k = 2;
        Runnable r = () -> counter[0] += k;
        r.run(); r.run();
        System.out.println(counter[0]);
        final String s1 = "ab";
        final String s2 = "a" + "b";
        String s3 = "a";
        String s4 = s3 + "b";
        System.out.println((s1 == s2) + " " + (s1 == "ab") + " " + (s4 == s1) + " " + s4.equals(s1) + " " + (s4.intern() == s1));
        final char c = 'a';
        byte b = c + 1;
        System.out.println(b);
        final long big = 1L << 40;
        System.out.println(big + " " + (int) big + " " + (big >>> 38));
    }
}
