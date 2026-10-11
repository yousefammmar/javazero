class Item {
    final int id = 1;
    void change() {
        id = 2;
    }
}
public class Main {
    public static void main(String[] args) {
        new Item().change();
    }
}
