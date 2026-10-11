import java.util.*;
public class Main {
    static class InsufficientFundsException extends RuntimeException {
        final double needed;
        InsufficientFundsException(double needed) { super("Need " + needed + " more"); this.needed = needed; }
    }
    static class Account {
        private double balance; private final String id;
        Account(String id, double b) {
            if (b < 0) throw new IllegalArgumentException("negative opening balance: " + b);
            this.id = id; this.balance = b;
        }
        void withdraw(double amt) {
            if (amt <= 0) throw new IllegalArgumentException("amount must be positive");
            if (amt > balance) throw new InsufficientFundsException(amt - balance);
            balance -= amt;
        }
        double balance() { return balance; }
    }
    static class QuietException extends RuntimeException {
        QuietException() { super(); }
        @Override public String getMessage() { return "overridden message"; }
    }
    static class NoMsg extends RuntimeException { }
    public static void main(String[] args) {
        Account acc = new Account("A1", 100);
        double[] tries = {30, 0, 500, -5, 70};
        for (double t : tries) {
            try { acc.withdraw(t); System.out.println("withdrew " + t + " -> " + acc.balance()); }
            catch (InsufficientFundsException e) { System.out.println("IFE " + e.getMessage() + " needed=" + e.needed); }
            catch (IllegalArgumentException e) { System.out.println("IAE " + e.getMessage()); }
        }
        try { new Account("B", -1); } catch (IllegalArgumentException e) { System.out.println(e); }
        try { throw new QuietException(); } catch (QuietException e) { System.out.println(e + " | " + e.getMessage() + " | " + e.getLocalizedMessage()); }
        try { throw new NoMsg(); } catch (NoMsg e) { System.out.println(e + " | " + e.getMessage()); }
        RuntimeException r = new InsufficientFundsException(1.5);
        System.out.println(r.getClass().getName());
        System.out.println(r.getClass().getSimpleName());
        System.out.println(r.getClass().getSuperclass().getName());
    }
}
