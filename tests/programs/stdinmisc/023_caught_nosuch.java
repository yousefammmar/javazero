import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("first " + sc.nextLine());
        System.out.println("int " + sc.nextInt());
        try {
            sc.nextInt();
            sc.nextInt();
        } catch (NoSuchElementException e) {
            System.out.println("caught " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        try {
            sc.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println("caught " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        try {
            sc.next();
        } catch (NoSuchElementException e) {
            System.out.println("caught " + e.getClass().getName());
        }
    }
}
