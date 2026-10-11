import java.util.*;
public class Main {
    static class Account {
        final String id; double balance; final List<String> log = new ArrayList<>();
        Account(String id, double b) { this.id = id; balance = b; }
        void deposit(double d) { if (d <= 0) throw new IllegalArgumentException("bad amount " + d); balance += d; log.add("D" + d); }
        void withdraw(double d) { if (d > balance) throw new IllegalStateException("insufficient funds in " + id); balance -= d; log.add("W" + d); }
    }
    public static void main(String[] args) {
        Map<String, Account> bank = new TreeMap<>();
        for (String id : new String[]{"acc3", "acc1", "acc2"}) bank.put(id, new Account(id, 100));
        String[][] ops = {{"acc1", "dep", "50"}, {"acc2", "wd", "30"}, {"acc3", "wd", "500"}, {"acc9", "dep", "1"}, {"acc1", "dep", "-5"}, {"acc2", "wd", "70"}, {"acc3", "dep", "0.5"}};
        for (String[] op : ops) {
            try {
                Account a = bank.get(op[0]);
                if (a == null) throw new NoSuchElementException("no account " + op[0]);
                double amt = Double.parseDouble(op[2]);
                if (op[1].equals("dep")) a.deposit(amt); else a.withdraw(amt);
                System.out.println("ok " + String.join(" ", op) + " -> " + a.balance);
            } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
                System.out.println("fail " + String.join(" ", op) + ": " + e.getClass().getSimpleName() + " " + e.getMessage());
            }
        }
        for (Account a : bank.values()) System.out.println(a.id + " " + a.balance + " " + a.log);
        Map<String, Integer> stock = new HashMap<>();
        String[] items = {"bolt", "nut", "screw", "washer", "gear", "spring", "cog", "pin"};
        for (int i = 0; i < items.length; i++) stock.put(items[i], (i + 1) * 12 % 25);
        System.out.println(stock);
        String[][] orders = {{"bolt", "5"}, {"gear", "100"}, {"cog", "3"}, {"cable", "1"}, {"pin", "12"}};
        List<String> backorder = new ArrayList<>();
        for (String[] o : orders) {
            int q = Integer.parseInt(o[1]);
            int have = stock.getOrDefault(o[0], 0);
            if (have >= q) stock.put(o[0], have - q);
            else { backorder.add(o[0] + ":" + (q - have)); stock.computeIfPresent(o[0], (k, v) -> 0); }
        }
        System.out.println(stock);
        System.out.println(backorder);
        stock.entrySet().removeIf(e -> e.getValue() == 0);
        System.out.println(stock);
        int total = 0;
        for (int v : stock.values()) total += v;
        System.out.println("total " + total + " low: " + stock.entrySet().stream().filter(e -> e.getValue() < 10).map(Map.Entry::getKey).sorted().toList());
    }
}
