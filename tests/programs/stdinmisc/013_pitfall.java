import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Age: ");
        int age = sc.nextInt();
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.println("age=" + age + " name=[" + name + "]");
        System.out.print("Name again: ");
        String name2 = sc.nextLine();
        System.out.println("name2=[" + name2 + "]");
        System.out.print("City: ");
        String city = sc.nextLine();
        System.out.println("city=[" + city + "]");
    }
}
