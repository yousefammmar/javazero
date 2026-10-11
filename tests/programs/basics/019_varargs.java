import java.util.Arrays;

public class Main {
    static int sum(int... xs) { int s = 0; for (int x : xs) s += x; return s; }
    static String desc(String label, int... xs) { return label + ":" + xs.length + Arrays.toString(xs); }
    static String objs(Object... os) { return os == null ? "null-array" : os.length + Arrays.toString(os); }
    static String two(int a, int b) { return "two ints"; }
    static String two(int... a) { return "varargs " + a.length; }
    static String mix(String s, Object... rest) { return s + rest.length; }
    static String mix(String s, String t) { return "exact " + s + t; }
    static double avg(double... v) { if (v.length == 0) return 0; double t = 0; for (double d : v) t += d; return t / v.length; }
    static String chars(char... cs) { return new String(cs); }
    static int count(String... ss) { return ss.length; }
    static String boxed(Integer... is) { return Arrays.toString(is); }
    static String lng(long... ls) { return "long" + ls.length; }
    static String lng(int a, int b) { return "int,int"; }
    static void mutate(int... xs) { if (xs.length > 0) xs[0] = 999; }
    public static void main(String[] args) {
        System.out.println(sum());
        System.out.println(sum(1));
        System.out.println(sum(1, 2, 3));
        System.out.println(sum(new int[]{4, 5, 6}));
        System.out.println(sum(new int[0]));
        System.out.println(desc("a"));
        System.out.println(desc("b", 7));
        System.out.println(desc("c", 7, 8, 9));
        System.out.println(objs());
        System.out.println(objs(1, "x", 2.5));
        System.out.println(objs((Object[]) null));
        System.out.println(objs((Object) null));
        System.out.println(objs(null, null));
        System.out.println(objs(new Object[]{1, 2}));
        System.out.println(objs((Object) new Object[]{1, 2}.length));
        System.out.println(objs((Object[]) new String[]{"s1", "s2"}));
        System.out.println(objs(new int[]{1, 2}).startsWith("1[[I@") ? "int[] is one Object" : "other");
        System.out.println(two(1, 2));
        System.out.println(two(1));
        System.out.println(two());
        System.out.println(two(1, 2, 3));
        System.out.println(mix("a", "b"));
        System.out.println(mix("a"));
        System.out.println(mix("a", 1));
        System.out.println(mix("a", "b", "c"));
        System.out.println(mix("a", (Object) "b"));
        System.out.println(avg());
        System.out.println(avg(1, 2));
        System.out.println(avg(1, 2.5f, 3L));
        System.out.println(chars('o', 'k'));
        System.out.println(chars());
        System.out.println(count() + " " + count("a") + " " + count("a", "b") + " " + count(new String[5]));
        System.out.println(boxed(1, 2, 3));
        System.out.println(boxed());
        System.out.println(lng(1, 2));
        System.out.println(lng(1L, 2));
        System.out.println(lng(1));
        int[] arr = {1, 2, 3};
        mutate(arr);
        System.out.println(arr[0]);
        mutate();
        System.out.println(String.format("%s-%s", "a", "b"));
        System.out.println(String.format("none"));
        System.out.println(String.format("%s %s", new Object[]{"p", "q"}));
        System.out.println(Arrays.asList(1, 2, 3).size() + " " + Arrays.asList(new Integer[]{1, 2}).size() + " " + Arrays.asList(new int[]{1, 2}).size());
        System.out.println(Math.max(3, 4) + Math.max(3L, 4) + Math.max(1.5, 2));
        System.out.println(String.join("/", "a", "b") + String.join("/", new String[]{"c", "d"}));
        System.out.println(java.util.List.of(1, 2, 3).size());
    }
}
