class Secret {
    private void hide() { System.out.println("hidden"); }
}
public class Main {
    public static void main(String[] args) {
        Secret s = new Secret();
        s.hide();
    }
}
