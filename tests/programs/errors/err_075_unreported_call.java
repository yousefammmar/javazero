public class Main {
    static void risky() throws Exception {
        throw new Exception("bad");
    }
    public static void main(String[] args) {
        risky();
    }
}
