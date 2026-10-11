import java.util.*;

public class Main {
    static abstract class Employee implements Comparable<Employee> {
        private static int counter = 1000;
        protected final int id;
        protected final String name;

        Employee(String name) { this.name = name; this.id = ++counter; }

        abstract double monthlyPay();
        String role() { return getClass().getSimpleName(); }

        @Override public int compareTo(Employee o) { return Double.compare(o.monthlyPay(), monthlyPay()); }
        @Override public String toString() {
            return String.format("%d %-8s %-10s %10.2f", id, name, role(), monthlyPay());
        }
    }

    static class Salaried extends Employee {
        private final double annual;
        Salaried(String n, double annual) { super(n); this.annual = annual; }
        double monthlyPay() { return annual / 12; }
    }

    static class Hourly extends Employee {
        private final double rate; private final int hours;
        Hourly(String n, double rate, int hours) { super(n); this.rate = rate; this.hours = hours; }
        double monthlyPay() {
            int overtime = Math.max(0, hours - 160);
            return (hours - overtime) * rate + overtime * rate * 1.5;
        }
    }

    static class Manager extends Salaried {
        private final List<Employee> reports = new ArrayList<>();
        Manager(String n, double annual) { super(n, annual); }
        void addReport(Employee e) { reports.add(e); }
        @Override double monthlyPay() { return super.monthlyPay() + 100 * reports.size(); }
        @Override String role() { return "Manager(" + reports.size() + ")"; }
    }

    static class Contractor extends Employee {
        private final double invoice;
        Contractor(String n, double invoice) { super(n); this.invoice = invoice; }
        double monthlyPay() { return invoice; }
    }

    public static void main(String[] args) {
        Manager boss = new Manager("Grace", 120000);
        Employee a = new Salaried("Linus", 84000);
        Employee b = new Hourly("Ada", 42.5, 172);
        Employee c = new Hourly("Alan", 30, 120);
        Employee d = new Contractor("Dennis", 6500.5);
        boss.addReport(a); boss.addReport(b); boss.addReport(c);
        List<Employee> staff = new ArrayList<>(List.of(a, b, c, d, boss));
        System.out.println("Roster:");
        for (Employee e : staff) System.out.println("  " + e);
        Collections.sort(staff);
        System.out.println("By pay:");
        for (Employee e : staff) System.out.println("  " + e.name + " " + String.format("%.2f", e.monthlyPay()));
        double total = 0;
        Map<String, Double> byRole = new TreeMap<>();
        for (Employee e : staff) {
            total += e.monthlyPay();
            byRole.merge(e.getClass().getSimpleName(), e.monthlyPay(), Double::sum);
        }
        System.out.printf("Total payroll: %,.2f%n", total);
        byRole.forEach((k, v) -> System.out.printf("  %-12s %,12.2f%n", k, v));
        for (Employee e : staff) {
            if (e instanceof Hourly h) System.out.println(h.name + " is hourly");
            else if (e instanceof Salaried s && !(e instanceof Manager)) System.out.println(s.name + " is salaried");
        }
        System.out.println("Manager is Salaried: " + (boss instanceof Salaried) + ", Employee: " + (boss instanceof Employee));
    }
}
