import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static void inc(int x) { x++; System.out.println("inside inc " + x); }
    static void incArr(int[] a) { a[0]++; a = new int[]{100}; a[0]++; System.out.println("inside incArr " + a[0]); }
    static void append(StringBuilder sb) { sb.append("!"); sb = new StringBuilder("new"); sb.append("?"); }
    static void str(String s) { s += "x"; s = s.toUpperCase(); }
    static void swap(int a, int b) { int t = a; a = b; b = t; }
    static void swapArr(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }
    static void fill(List<Integer> l) { l.add(l.size()); }
    static int[] grow(int[] a) { return Arrays.copyOf(a, a.length + 1); }
    static void boxed(Integer i) { i++; }
    static int sideEffects = 0;
    static int se(int v) { sideEffects = sideEffects * 10 + v; return v; }
    static int combine(int a, int b, int c) { return a * 100 + b * 10 + c; }
    static class P { int v; P(int v) { this.v = v; } }
    static void mod(P p) { p.v++; p = new P(50); p.v++; }
    static P replace(P p) { return new P(p.v * 2); }
    static void modLocal() { int local = 1; { int inner = local + 1; local = inner; } System.out.println("local " + local); }
    public static void main(String[] args) {
        int x = 5;
        inc(x);
        System.out.println(x);
        int[] arr = {1, 2};
        incArr(arr);
        System.out.println(arr[0]);
        StringBuilder sb = new StringBuilder("sb");
        append(sb);
        System.out.println(sb);
        String s = "str";
        str(s);
        System.out.println(s);
        int p = 1, q = 2;
        swap(p, q);
        System.out.println(p + " " + q);
        int[] sw = {1, 2, 3};
        swapArr(sw, 0, 2);
        System.out.println(Arrays.toString(sw));
        List<Integer> li = new ArrayList<>();
        fill(li); fill(li); fill(li);
        System.out.println(li);
        int[] g = {1};
        int[] g2 = grow(g);
        System.out.println(g.length + " " + g2.length);
        Integer bx = 9;
        boxed(bx);
        System.out.println(bx);
        System.out.println(combine(se(1), se(2), se(3)) + " " + sideEffects);
        sideEffects = 0;
        int r = se(4) + se(5) * se(6);
        System.out.println(r + " " + sideEffects);
        P pp = new P(1);
        mod(pp);
        System.out.println(pp.v);
        P p2 = replace(pp);
        System.out.println(pp.v + " " + p2.v + " " + (pp == p2));
        modLocal();
        P[] ps = {new P(1), new P(2)};
        for (P e : ps) e.v *= 10;
        for (P e : ps) e = new P(0);
        System.out.println(ps[0].v + " " + ps[1].v);
        int[] nums = {1, 2, 3};
        for (int n : nums) n *= 2;
        System.out.println(Arrays.toString(nums));
        for (int i = 0; i < nums.length; i++) nums[i] *= 2;
        System.out.println(Arrays.toString(nums));
        int[] a1 = {1, 2}, a2 = a1;
        a2[0] = 9;
        a1 = new int[]{7, 7};
        System.out.println(Arrays.toString(a1) + Arrays.toString(a2));
        String s1 = "a", s2 = s1;
        s1 = "b";
        System.out.println(s1 + s2);
        List<Integer> l1 = new ArrayList<>(List.of(1, 2)), l2 = l1, l3 = new ArrayList<>(l1);
        l1.add(3);
        System.out.println(l2 + " " + l3);
        long big = x;
        big += Integer.MAX_VALUE;
        System.out.println(big + " " + x);
    }
}
