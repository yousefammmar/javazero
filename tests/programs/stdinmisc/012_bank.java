import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double balance = 100.0;
        boolean run = true;
        while (run && sc.hasNext()) {
            System.out.print("1) Deposit 2) Withdraw 3) Balance 4) Exit: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1: {
                    double amt = sc.nextDouble();
                    balance += amt;
                    System.out.println("Deposited " + amt);
                    break;
                }
                case 2: {
                    double amt = sc.nextDouble();
                    if (amt > balance) System.out.println("Insufficient funds");
                    else { balance -= amt; System.out.println("Withdrew " + amt); }
                    break;
                }
                case 3:
                    System.out.printf("Balance: %.2f%n", balance);
                    break;
                case 4:
                    run = false;
                    break;
                default:
                    System.out.println("Invalid choice " + choice);
            }
        }
        System.out.println("Final: " + balance);
    }
}
