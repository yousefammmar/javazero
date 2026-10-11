import java.util.*;
public class Main {
    public static void main(String[] args) {
        Integer a = 127, b = 127, c = 128, d = 128;
        System.out.println((a == b) + " " + (c == d) + " " + c.equals(d) + " " + (c.intValue() == d) + " " + (c == 128));
        Integer e = 1000, f = 1000;
        System.out.println((e == f) + " " + e.equals(f) + " " + (e <= f && e >= f));
        Long l1 = 127L, l2 = 127L, l3 = 128L, l4 = 128L;
        System.out.println((l1 == l2) + " " + (l3 == l4) + " " + l3.equals(l4) + " " + l1.equals(127) + " " + l1.equals(127L));
        Integer n1 = Integer.valueOf(-128), n2 = Integer.valueOf(-128), n3 = Integer.valueOf(-129), n4 = Integer.valueOf(-129);
        System.out.println((n1 == n2) + " " + (n3 == n4));
        List<Integer> list = new ArrayList<>(List.of(1000, 1000, 5, 5));
        System.out.println((list.get(0) == list.get(1)) + " " + list.get(0).equals(list.get(1)) + " " + (list.get(2) == list.get(3)));
        System.out.println(list.get(0) == 1000);
        Map<String, Integer> m = new HashMap<>();
        m.put("a", 1000); m.put("b", 1000);
        System.out.println((m.get("a") == m.get("b")) + " " + m.get("a").equals(m.get("b")) + " " + ((int) m.get("a") == m.get("b")));
        try { int x = m.get("zzz"); System.out.println(x); } catch (NullPointerException ex) { System.out.println("NPE unboxing null"); }
        Integer big = null;
        try { if (big > 3) System.out.println("?"); } catch (NullPointerException ex) { System.out.println("NPE compare"); }
        Character c1 = 'a', c2 = 'a';
        System.out.println((c1 == c2) + " " + (Character.valueOf('ሴ') == Character.valueOf('ሴ')));
        Boolean b1 = true, b2 = Boolean.valueOf("TRUE");
        System.out.println((b1 == b2) + " " + b1.equals(b2));
        Double d1 = 0.5, d2 = 0.5;
        System.out.println((d1 == d2) + " " + d1.equals(d2));
        Object o = 5;
        System.out.println(o.getClass().getSimpleName() + " " + ((Object) 5L).getClass().getSimpleName() + " " + ((Object) 'x').getClass().getSimpleName() + " " + ((Object) 1.5f).getClass().getSimpleName() + " " + ((Object) (short) 3).getClass().getSimpleName() + " " + ((Object) (byte) 3).getClass().getSimpleName());
        List<Long> longs = new ArrayList<>();
        longs.add(5L);
        System.out.println(longs.contains(5) + " " + longs.contains(5L) + " " + longs.indexOf(5L));
        Set<Short> shorts = new HashSet<>();
        for (short i = 0; i < 5; i++) { shorts.add(i); shorts.remove(i - 1); }
        System.out.println(shorts);
        int sum = 0;
        Integer boxedSum = 0;
        for (int i = 0; i < 5; i++) { sum += i; boxedSum += i; }
        System.out.println(sum + " " + boxedSum);
        Integer x1 = 10; Integer x2 = x1; x1++;
        System.out.println(x1 + " " + x2);
        List<Integer> li = new ArrayList<>(List.of(10, 20, 30));
        int idx = 1;
        li.remove(idx);
        li.remove(Integer.valueOf(10));
        System.out.println(li);
        System.out.println(Integer.valueOf(65).equals(65) + " " + new ArrayList<>(List.of(65)).contains((int) 'A') + " " + (char) (int) li.get(0));
        double avg = (double) (1 + 2) / 2;
        System.out.println(avg + " " + (1 + 2) / 2 + " " + Integer.compare(c, d) + " " + c.compareTo(a));
    }
}
