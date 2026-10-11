import java.util.*;

public class Main {
    interface Discount {
        double apply(double subtotal);
        String describe();
    }

    static class NoDiscount implements Discount {
        public double apply(double s) { return s; }
        public String describe() { return "none"; }
    }

    static class PercentOff implements Discount {
        private final int pct;
        PercentOff(int pct) { this.pct = pct; }
        public double apply(double s) { return s * (100 - pct) / 100.0; }
        public String describe() { return pct + "% off"; }
    }

    static class FlatOff implements Discount {
        private final double amt;
        FlatOff(double amt) { this.amt = amt; }
        public double apply(double s) { return Math.max(0, s - amt); }
        public String describe() { return "$" + amt + " off"; }
    }

    static class BulkDiscount implements Discount {
        public double apply(double s) { return s > 100 ? s * 0.85 : s > 50 ? s * 0.95 : s; }
        public String describe() { return "bulk tiers"; }
    }

    record Line(String name, int qty, double unit) {
        double total() { return qty * unit; }
    }

    static class Cart {
        private final List<Line> lines = new ArrayList<>();
        private Discount discount = new NoDiscount();
        void add(String n, int q, double p) { lines.add(new Line(n, q, p)); }
        void setDiscount(Discount d) { discount = d; }
        double subtotal() {
            double s = 0;
            for (Line l : lines) s += l.total();
            return s;
        }
        void print() {
            for (Line l : lines) System.out.printf("%-12s %2d x %7.2f = %8.2f%n", l.name(), l.qty(), l.unit(), l.total());
            double sub = subtotal(), after = discount.apply(sub), tax = after * 0.08;
            System.out.printf("%-12s %27.2f%n", "Subtotal", sub);
            System.out.printf("%-12s %27.2f  [%s]%n", "Discounted", after, discount.describe());
            System.out.printf("%-12s %27.2f%n", "Tax (8%)", tax);
            System.out.printf("%-12s %27.2f%n", "TOTAL", after + tax);
        }
    }

    public static void main(String[] args) {
        Cart cart = new Cart();
        cart.add("Notebook", 3, 4.99);
        cart.add("Pen pack", 2, 3.5);
        cart.add("Backpack", 1, 39.95);
        Discount[] options = {new NoDiscount(), new PercentOff(10), new FlatOff(15), new BulkDiscount(),
            new Discount() {
                public double apply(double s) { return s - (int) (s / 20) * 2; }
                public String describe() { return "$2 per $20 spent"; }
            }};
        for (Discount d : options) {
            System.out.println("=== " + d.describe() + " ===");
            cart.setDiscount(d);
            cart.print();
        }
        cart.add("Desk lamp", 4, 22.0);
        System.out.println("=== after adding lamps ===");
        cart.setDiscount(new BulkDiscount());
        cart.print();
        Discount lambdaLike = new PercentOff(50);
        System.out.println(lambdaLike.apply(99.98));
    }
}
