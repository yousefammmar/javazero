import java.util.*;

public class Main {
    record Row(String item, int qty, double unit, boolean taxable) {}

    static String center(String s, int w) {
        int pad = w - s.length();
        if (pad <= 0) return s;
        int left = pad / 2;
        return " ".repeat(left) + s + " ".repeat(pad - left);
    }

    static String money(double v) { return String.format("$%,.2f", v); }

    public static void main(String[] args) {
        List<Row> rows = List.of(
            new Row("Espresso machine", 1, 1299.99, true),
            new Row("Coffee beans 1kg", 12, 18.5, false),
            new Row("Milk frother", 2, 45.0, true),
            new Row("Descaler", 6, 7.25, false),
            new Row("Cups (box of 50)", 20, 12.8, true));
        int width = 62;
        String bar = "=".repeat(width);
        System.out.println(bar);
        System.out.println(center("INVOICE #2024-017", width));
        System.out.println(center("Acme Coffee Supplies", width));
        System.out.println(bar);
        System.out.printf("%-20s %5s %12s %12s %5s%n", "Item", "Qty", "Unit", "Amount", "Tax");
        System.out.println("-".repeat(width));
        double sub = 0, tax = 0;
        for (Row r : rows) {
            double amt = r.qty() * r.unit();
            sub += amt;
            if (r.taxable()) tax += amt * 0.0725;
            System.out.printf("%-20s %5d %12s %12s %5s%n", r.item(), r.qty(), money(r.unit()), money(amt), r.taxable() ? "T" : "-");
        }
        System.out.println("-".repeat(width));
        System.out.printf("%39s %12s%n", "Subtotal:", money(sub));
        System.out.printf("%39s %12s%n", "Tax (7.25%):", money(tax));
        System.out.printf("%39s %12s%n", "TOTAL:", money(sub + tax));
        System.out.println(bar);
        System.out.println(String.format("|%10s|%-10s|%010d|%+.1f|%,d|", "abc", "abc", 42, 2.55, 1234567));
        System.out.println(String.format("%5.1f%%  %b  %c  %S  %08.2f  %X  %5s|%-5s|", 99.5, true, 'z', "shout", -3.14159, 48879, null, false));
        System.out.println(String.format("%3$s %1$s %2$s", "a", "b", "c"));
        System.out.println(String.format("%.0f %.0f %.0f %.1f %.2f", 0.5, 1.5, 2.5, 0.05, 1.005));
        System.out.println(String.format("%10.3e|%-12.4s|%h", 123456.789, "truncated text", "hi"));
        System.out.println("Name".concat(":").repeat(2) + " " + "  trim me  ".strip() + "|" + "a,b,,c".split(",").length);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) sb.append(String.format("%-4d", i * i * i));
        System.out.println(sb.toString().stripTrailing() + "|");
    }
}
