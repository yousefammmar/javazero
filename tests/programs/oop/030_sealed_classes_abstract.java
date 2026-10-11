import java.util.*;

public class Main {
    sealed static abstract class Account permits Savings, Checking, Premium {
        private static int nextId = 1000;
        protected final int id; protected long balance;
        Account(long initial) { id = nextId++; balance = initial; }
        abstract long fee();
        void withdraw(long amt) {
            long total = amt + fee();
            if (total > balance) throw new IllegalStateException("insufficient funds in " + id + ": need " + total + " have " + balance);
            balance -= total;
        }
        void deposit(long amt) { balance += amt; }
        public String toString() { return getClass().getSimpleName() + "#" + id + "[" + balance + "]"; }
    }
    static final class Savings extends Account { Savings(long i) { super(i); } long fee() { return 5; } }
    static final class Checking extends Account { Checking(long i) { super(i); } long fee() { return 1; } }
    static non-sealed class Premium extends Account { Premium(long i) { super(i); } long fee() { return 0; } }
    static class Gold extends Premium { Gold(long i) { super(i); } @Override void deposit(long amt) { super.deposit(amt + amt / 10); } }
    static String tier(Account a) {
        return switch (a) {
            case Savings s -> "S";
            case Checking c -> "C";
            case Gold g -> "G";
            case Premium p -> "P";
        };
    }
    public static void main(String[] args) {
        List<Account> accts = List.of(new Savings(100), new Checking(50), new Premium(10), new Gold(0));
        for (Account a : accts) System.out.println(a + " tier=" + tier(a) + " fee=" + a.fee());
        for (Account a : accts) {
            try { a.withdraw(20); System.out.println("ok " + a); }
            catch (IllegalStateException e) { System.out.println(e.getMessage()); }
        }
        for (Account a : accts) a.deposit(100);
        System.out.println(accts);
        long sum = 0;
        for (Account a : accts) sum += a.balance;
        System.out.println(sum);
        Account nextAcct = new Checking(1);
        System.out.println(nextAcct.id);
    }
}
