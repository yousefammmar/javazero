enum Color { RED, GREEN }
public class Main {
    public static void main(String[] args) {
        Color c = Color.RED;
        switch (c) {
            case RED: System.out.println("r"); break;
            case BLUE: System.out.println("b"); break;
        }
    }
}
