import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> items = new ArrayList<>();
        while (true) {
            System.out.print("> ");
            if (!sc.hasNextLine()) { System.out.println("EOF"); break; }
            String cmd = sc.nextLine().trim();
            if (cmd.equalsIgnoreCase("quit")) { System.out.println("Bye"); break; }
            if (cmd.startsWith("add ")) { items.add(cmd.substring(4)); System.out.println("Added " + cmd.substring(4)); }
            else if (cmd.equals("list")) {
                for (int i = 0; i < items.size(); i++) System.out.println((i + 1) + ". " + items.get(i));
            } else if (cmd.equals("count")) System.out.println(items.size());
            else if (cmd.startsWith("del ")) {
                int idx = Integer.parseInt(cmd.substring(4).trim());
                System.out.println("Removed " + items.remove(idx - 1));
            } else System.out.println("Unknown: '" + cmd + "'");
        }
        System.out.println(items);
    }
}
