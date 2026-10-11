import java.util.*;
public class Main {
    static class Version implements Comparable<Version> {
        final int major, minor;
        Version(int a, int b) { major = a; minor = b; }
        @Override public int compareTo(Version o) { return major != o.major ? Integer.compare(major, o.major) : Integer.compare(minor, o.minor); }
        @Override public String toString() { return major + "." + minor; }
        @Override public boolean equals(Object o) { return o instanceof Version v && compareTo(v) == 0; }
        @Override public int hashCode() { return major * 100 + minor; }
    }
    static class Student implements Comparable<Student> {
        String name; double gpa;
        Student(String n, double g) { name = n; gpa = g; }
        public int compareTo(Student o) { return Double.compare(o.gpa, gpa); }
        public String toString() { return name + ":" + gpa; }
    }
    public static void main(String[] args) {
        List<Version> vs = new ArrayList<>();
        int[][] raw = {{1, 10}, {1, 2}, {2, 0}, {1, 2}, {0, 9}, {2, 1}};
        for (int[] r : raw) vs.add(new Version(r[0], r[1]));
        System.out.println(vs);
        Collections.sort(vs);
        System.out.println(vs);
        TreeSet<Version> ts = new TreeSet<>(vs);
        System.out.println(ts + " " + ts.size());
        System.out.println(ts.first() + " " + ts.last() + " " + ts.floor(new Version(1, 5)) + " " + ts.higher(new Version(1, 10)));
        TreeMap<Version, String> tm = new TreeMap<>();
        for (Version v : vs) tm.put(v, "rel-" + v);
        System.out.println(tm);
        Set<Version> hs = new HashSet<>(vs);
        System.out.println(hs.size() + " " + hs.contains(new Version(2, 1)));
        System.out.println(Collections.max(vs) + " " + Collections.min(vs));
        System.out.println(Collections.binarySearch(vs, new Version(2, 0)));
        List<Student> st = new ArrayList<>(List.of(new Student("A", 3.1), new Student("B", 3.9), new Student("C", 2.5), new Student("D", 3.9)));
        Collections.sort(st);
        System.out.println(st);
        PriorityQueue<Student> pq = new PriorityQueue<>(st);
        System.out.println(pq.peek() + " " + pq.size());
        Student[] arr = st.toArray(new Student[0]);
        Arrays.sort(arr, Collections.reverseOrder());
        System.out.println(Arrays.toString(arr));
        System.out.println(new Version(1, 2).compareTo(new Version(1, 3)) + " " + new Version(3, 0).compareTo(new Version(1, 3)));
        try {
            TreeSet<Object> bad = new TreeSet<>();
            bad.add(new Object());
        } catch (ClassCastException e) { System.out.println("ClassCastException"); }
    }
}
