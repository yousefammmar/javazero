import java.util.*;
public class Main {
    static int[] twoSum(int[] a, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < a.length; i++) {
            Integer j = seen.get(target - a[i]);
            if (j != null) return new int[]{j, i};
            seen.put(a[i], i);
        }
        return new int[0];
    }
    public static void main(String[] args) {
        int[] a = {2, 7, 11, 15, -3, 8, 20, 4};
        for (int t : new int[]{9, 17, 5, 100, 12, 1}) System.out.println("target " + t + " -> " + Arrays.toString(twoSum(a, t)));
        // longest consecutive
        int[] cons = {100, 4, 200, 1, 3, 2, 101, 102, 5};
        Set<Integer> set = new HashSet<>();
        for (int x : cons) set.add(x);
        int best = 0;
        for (int x : set) if (!set.contains(x - 1)) { int len = 1; while (set.contains(x + len)) len++; best = Math.max(best, len); }
        System.out.println("longest consecutive " + best + " from " + set);
        // first non-repeating char
        String s = "swiss cheese is tasty";
        Map<Character, Integer> cnt = new LinkedHashMap<>();
        for (char c : s.toCharArray()) cnt.merge(c, 1, Integer::sum);
        for (Map.Entry<Character, Integer> e : cnt.entrySet()) if (e.getValue() == 1) { System.out.println("first unique " + e.getKey()); break; }
        System.out.println(cnt);
        // subarray sum equals k
        int[] arr = {3, 4, 7, 2, -3, 1, 4, 2};
        int k = 7, sum = 0, count = 0;
        Map<Integer, Integer> prefix = new HashMap<>();
        prefix.put(0, 1);
        for (int x : arr) { sum += x; count += prefix.getOrDefault(sum - k, 0); prefix.merge(sum, 1, Integer::sum); }
        System.out.println("subarrays summing to 7: " + count + " " + prefix);
        // isomorphic
        String[][] pairs = {{"egg", "add"}, {"foo", "bar"}, {"paper", "title"}};
        for (String[] p : pairs) {
            Map<Character, Character> m1 = new HashMap<>(), m2 = new HashMap<>();
            boolean ok = true;
            for (int i = 0; i < p[0].length(); i++) {
                char x = p[0].charAt(i), y = p[1].charAt(i);
                if (m1.containsKey(x) && m1.get(x) != y || m2.containsKey(y) && m2.get(y) != x) { ok = false; break; }
                m1.put(x, y); m2.put(y, x);
            }
            System.out.println(p[0] + "/" + p[1] + " " + ok);
        }
        // intersection of arrays
        Set<Integer> s1 = new HashSet<>(List.of(1, 2, 2, 3, 4)), res = new TreeSet<>();
        for (int x : new int[]{2, 2, 4, 6}) if (s1.contains(x)) res.add(x);
        System.out.println(res);
        // majority element
        int[] maj = {3, 3, 4, 2, 3, 3, 1, 3};
        Map<Integer, Integer> mc = new HashMap<>();
        for (int x : maj) if (mc.merge(x, 1, Integer::sum) > maj.length / 2) System.out.println("majority " + x);
    }
}
