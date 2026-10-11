import java.util.*;

public class Main {
    static final class Money {
        private final long cents; private final String cur;
        private Money(long c, String cur) { cents = c; this.cur = cur; }
        static Money of(long c, String cur) { if (c < 0) throw new IllegalArgumentException("negative: " + c); return new Money(c, cur); }
        Money plus(Money o) { if (!cur.equals(o.cur)) throw new IllegalStateException("currency mismatch " + cur + "/" + o.cur); return new Money(cents + o.cents, cur); }
        Money times(int k) { return new Money(cents * k, cur); }
        @Override public boolean equals(Object o) { return o instanceof Money m && m.cents == cents && m.cur.equals(cur); }
        @Override public int hashCode() { return Long.hashCode(cents) * 31 + cur.hashCode(); }
        @Override public String toString() { return String.format("%d.%02d %s", cents / 100, cents % 100, cur); }
    }
    static class Pizza {
        private final String size; private final List<String> toppings; private final boolean extraCheese;
        private Pizza(Builder b) { size = b.size; toppings = List.copyOf(b.toppings); extraCheese = b.extraCheese; }
        static Builder builder() { return new Builder(); }
        static class Builder {
            private String size = "M"; private final List<String> toppings = new ArrayList<>(); private boolean extraCheese;
            Builder size(String s) { size = s; return this; }
            Builder topping(String t) { toppings.add(t); return this; }
            Builder extraCheese() { extraCheese = true; return this; }
            Pizza build() { if (toppings.size() > 3) throw new IllegalStateException("too many toppings"); return new Pizza(this); }
        }
        public String toString() { return "Pizza(" + size + ", " + toppings + (extraCheese ? ", +cheese" : "") + ")"; }
    }
    public static void main(String[] args) {
        Money a = Money.of(1050, "EUR"), b = Money.of(295, "EUR");
        System.out.println(a + " + " + b + " = " + a.plus(b));
        System.out.println(a.times(3) + " " + a.equals(Money.of(1050, "EUR")) + " " + (a == a.plus(Money.of(0, "EUR"))));
        System.out.println(a.plus(b).equals(b.plus(a)));
        try { a.plus(Money.of(1, "USD")); } catch (IllegalStateException e) { System.out.println("ISE " + e.getMessage()); }
        try { Money.of(-5, "EUR"); } catch (IllegalArgumentException e) { System.out.println("IAE " + e.getMessage()); }
        Set<Money> set = new HashSet<>(List.of(a, b));
        System.out.println(set.contains(Money.of(295, "EUR")) + " " + set.contains(Money.of(295, "USD")));
        Pizza.Builder bld = Pizza.builder().size("L").topping("ham").topping("olive");
        Pizza p1 = bld.build();
        bld.topping("onion").extraCheese();
        Pizza p2 = bld.build();
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(Pizza.builder().build());
        try { bld.topping("x").build(); } catch (IllegalStateException e) { System.out.println("ISE " + e.getMessage()); }
        try { p1.toppings.add("boom"); } catch (UnsupportedOperationException e) { System.out.println("UOE immutable list"); }
        String s = "abc"; String t = s.toUpperCase();
        System.out.println(s + " " + t + " " + (s == "abc") + " " + new String("abc").equals(s));
    }
}
