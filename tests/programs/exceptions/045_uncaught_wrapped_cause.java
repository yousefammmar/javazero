public class Main {
    public static void main(String[] args) {
        System.out.println("before");
        try {
            Object o = "str";
            Integer i = (Integer) o;
        } catch (ClassCastException e) {
            System.out.println("wrapping");
            throw new RuntimeException(e);
        } finally {
            System.out.println("finally");
        }
    }
}
