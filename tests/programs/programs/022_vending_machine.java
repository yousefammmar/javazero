import java.util.*;

public class Main {
    enum State { IDLE, HAS_MONEY, DISPENSING, OUT_OF_ORDER }

    static class Product {
        final String name; final int price; int stock;
        Product(String n, int p, int s) { name = n; price = p; stock = s; }
    }

    static class Machine {
        State state = State.IDLE;
        int credit = 0;
        final Map<String, Product> products = new LinkedHashMap<>();
        int sales = 0;

        void insert(int cents) {
            switch (state) {
                case IDLE, HAS_MONEY -> {
                    credit += cents;
                    state = State.HAS_MONEY;
                    System.out.println("Inserted " + cents + ", credit=" + credit);
                }
                case DISPENSING -> System.out.println("Busy dispensing");
                case OUT_OF_ORDER -> System.out.println("Out of order, returning " + cents);
            }
        }

        void select(String code) {
            if (state != State.HAS_MONEY) { System.out.println("Insert money first (state=" + state + ")"); return; }
            Product p = products.get(code);
            if (p == null) { System.out.println("Unknown code " + code); return; }
            if (p.stock == 0) { System.out.println(p.name + " sold out"); return; }
            if (credit < p.price) { System.out.println("Need " + (p.price - credit) + " more for " + p.name); return; }
            state = State.DISPENSING;
            p.stock--;
            credit -= p.price;
            sales += p.price;
            System.out.println("Dispensing " + p.name);
            state = State.HAS_MONEY;
            if (credit == 0) state = State.IDLE;
        }

        void refund() {
            if (credit > 0) {
                int[] coins = {100, 25, 10, 5};
                StringBuilder sb = new StringBuilder();
                int c = credit;
                for (int coin : coins) {
                    int n = c / coin;
                    if (n > 0) { sb.append(n).append("x").append(coin).append(" "); c -= n * coin; }
                }
                System.out.println("Refund " + credit + ": " + sb.toString().trim());
            }
            credit = 0;
            if (state != State.OUT_OF_ORDER) state = State.IDLE;
        }
    }

    public static void main(String[] args) {
        Machine m = new Machine();
        m.products.put("A1", new Product("Chips", 75, 2));
        m.products.put("A2", new Product("Candy", 60, 1));
        m.products.put("B1", new Product("Soda", 125, 5));
        m.select("A1");
        m.insert(100);
        m.select("A1");
        m.insert(25);
        m.select("B1");
        m.insert(100);
        m.select("B1");
        m.select("Z9");
        m.refund();
        m.insert(100);
        m.select("A2");
        m.insert(100);
        m.select("A2");
        m.refund();
        m.state = State.OUT_OF_ORDER;
        m.insert(25);
        for (Product p : m.products.values()) System.out.println(p.name + " stock=" + p.stock);
        System.out.println("Sales: " + m.sales / 100 + "." + String.format("%02d", m.sales % 100));
        System.out.println(Arrays.toString(State.values()));
    }
}
