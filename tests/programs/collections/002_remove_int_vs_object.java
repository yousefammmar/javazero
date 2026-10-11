import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<Integer> a = new ArrayList<>(List.of(3, 1, 4, 1, 5, 9, 2, 6));
        a.remove(1);
        System.out.println("remove(1) index: " + a);
        a.remove(Integer.valueOf(1));
        System.out.println("remove(valueOf 1): " + a);
        int idx = 0;
        a.remove(idx);
        System.out.println("remove(int var): " + a);
        Integer boxed = 9;
        a.remove(boxed);
        System.out.println("remove(Integer var): " + a);
        System.out.println("remove returns " + a.remove(Integer.valueOf(100)));
        Object o = 5;
        System.out.println("remove(Object) returns " + a.remove(o));
        System.out.println(a);
        List<Integer> b = new ArrayList<>();
        for (int i = 0; i < 10; i++) b.add(i);
        for (int i = 0; i < b.size(); i++) if (b.get(i) % 2 == 0) b.remove(i);
        System.out.println("skip-bug removal: " + b);
        List<Integer> c = new ArrayList<>();
        for (int i = 0; i < 10; i++) c.add(i);
        for (int i = c.size() - 1; i >= 0; i--) if (c.get(i) % 2 == 0) c.remove(i);
        System.out.println("backward removal: " + c);
        Set<Integer> set = new HashSet<>(List.of(1, 2, 3));
        set.remove(1);
        System.out.println("set remove(1): " + set);
        List<Character> cs = new ArrayList<>(List.of('a', 'b', 'c'));
        cs.remove(Character.valueOf('b'));
        System.out.println(cs);
        List<Long> ls = new ArrayList<>(List.of(1L, 2L, 3L));
        ls.remove(1L);
        System.out.println("Long remove(1L) object: " + ls);
        ls.remove(0);
        System.out.println("Long remove(0) index: " + ls);
        List<String> ss = new ArrayList<>(List.of("x", "y", "z"));
        System.out.println(ss.remove("y") + " " + ss.remove(0) + " " + ss);
    }
}
