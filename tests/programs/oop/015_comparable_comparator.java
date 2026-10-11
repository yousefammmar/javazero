import java.util.*;

public class Main {
    static class Version implements Comparable<Version> {
        final int major, minor;
        Version(int a, int b) { major = a; minor = b; }
        public int compareTo(Version o) { return major != o.major ? Integer.compare(major, o.major) : Integer.compare(minor, o.minor); }
        @Override public boolean equals(Object o) { return o instanceof Version v && v.major == major && v.minor == minor; }
        @Override public int hashCode() { return major * 31 + minor; }
        public String toString() { return major + "." + minor; }
    }
    record Emp(String name, String dept, int sal) {}
    public static void main(String[] args) {
        List<Version> vs = new ArrayList<>(List.of(new Version(1, 10), new Version(1, 2), new Version(0, 9), new Version(2, 0), new Version(1, 2)));
        Collections.sort(vs);
        System.out.println(vs);
        System.out.println(Collections.max(vs) + " " + Collections.min(vs));
        vs.sort(Comparator.reverseOrder());
        System.out.println(vs);
        System.out.println(new Version(1, 2).compareTo(new Version(1, 3)) + " " + new Version(3, 0).compareTo(new Version(1, 3)) + " " + new Version(1, 3).compareTo(new Version(1, 3)));
        TreeSet<Version> ts = new TreeSet<>(vs);
        System.out.println(ts + " " + ts.first() + " " + ts.last() + " " + ts.ceiling(new Version(1, 5)) + " " + ts.floor(new Version(1, 5)));
        TreeMap<Version, String> tm = new TreeMap<>();
        for (Version v : vs) tm.put(v, "v" + v);
        System.out.println(tm);
        List<Emp> emps = new ArrayList<>(List.of(new Emp("Zed", "eng", 100), new Emp("Amy", "eng", 120), new Emp("Bob", "ops", 90), new Emp("Cat", "ops", 90), new Emp("Dan", "eng", 100)));
        emps.sort(Comparator.comparing(Emp::dept).thenComparing(Emp::sal, Comparator.reverseOrder()).thenComparing(Emp::name));
        for (Emp e : emps) System.out.println(e);
        emps.sort(Comparator.comparingInt(Emp::sal).thenComparing(Emp::name, Comparator.reverseOrder()));
        System.out.println(emps.stream().map(Emp::name).toList());
        Comparator<String> byLen = (x, y) -> x.length() - y.length();
        String[] words = { "pear", "fig", "banana", "kiwi", "apple", "date" };
        Arrays.sort(words, byLen);
        System.out.println(Arrays.toString(words));
        Arrays.sort(words, byLen.reversed().thenComparing(Comparator.naturalOrder()));
        System.out.println(Arrays.toString(words));
        Arrays.sort(words, String.CASE_INSENSITIVE_ORDER);
        System.out.println(Arrays.toString(words));
        Integer[] nums = { 5, 3, 9, 1 };
        Arrays.sort(nums, Collections.reverseOrder());
        System.out.println(Arrays.toString(nums));
        List<String> withNull = new ArrayList<>(Arrays.asList("b", null, "a"));
        withNull.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        System.out.println(withNull);
        withNull.sort(Comparator.nullsLast(Comparator.reverseOrder()));
        System.out.println(withNull);
    }
}
