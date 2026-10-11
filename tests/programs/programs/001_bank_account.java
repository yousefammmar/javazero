import java.util.*;

public class Main {
    static class InsufficientFundsException extends Exception {
        private final double shortfall;
        public InsufficientFundsException(String msg, double shortfall) {
            super(msg);
            this.shortfall = shortfall;
        }
        public double getShortfall() { return shortfall; }
    }

    enum TxType { DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT }

    record Transaction(int id, TxType type, double amount, double balanceAfter) {
        @Override
        public String toString() {
            return String.format("#%03d %-12s %10.2f -> %10.2f", id, type, amount, balanceAfter);
        }
    }

    static class Account {
        private static int nextTx = 1;
        private final String owner;
        private double balance;
        private final List<Transaction> log = new ArrayList<>();

        Account(String owner, double initial) {
            this.owner = owner;
            this.balance = initial;
        }

        void deposit(double amt) {
            if (amt <= 0) throw new IllegalArgumentException("Deposit must be positive: " + amt);
            balance += amt;
            log.add(new Transaction(nextTx++, TxType.DEPOSIT, amt, balance));
        }

        void withdraw(double amt) throws InsufficientFundsException {
            if (amt > balance) {
                throw new InsufficientFundsException(owner + " cannot withdraw " + amt, amt - balance);
            }
            balance -= amt;
            log.add(new Transaction(nextTx++, TxType.WITHDRAW, amt, balance));
        }

        void transferTo(Account other, double amt) throws InsufficientFundsException {
            if (amt > balance) throw new InsufficientFundsException("Transfer failed for " + owner, amt - balance);
            balance -= amt;
            log.add(new Transaction(nextTx++, TxType.TRANSFER_OUT, amt, balance));
            other.balance += amt;
            other.log.add(new Transaction(nextTx++, TxType.TRANSFER_IN, amt, other.balance));
        }

        void printLog() {
            System.out.println("--- " + owner + " ---");
            for (Transaction t : log) System.out.println(t);
            System.out.printf("Final balance: %.2f%n", balance);
        }
    }

    public static void main(String[] args) {
        Account alice = new Account("Alice", 100.0);
        Account bob = new Account("Bob", 50.5);
        try {
            alice.deposit(250.75);
            alice.withdraw(75.25);
            alice.transferTo(bob, 100);
            bob.withdraw(20);
            bob.withdraw(500);
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.printf("Short by %.2f%n", e.getShortfall());
        }
        try {
            alice.deposit(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("finally block executed");
        }
        try {
            alice.transferTo(bob, 10000);
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage() + " short=" + e.getShortfall());
        }
        alice.printLog();
        bob.printLog();
    }
}
