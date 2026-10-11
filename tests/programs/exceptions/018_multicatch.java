public class Main {
    static void go(int k) {
        try {
            switch (k) {
                case 0: Integer.parseInt("x"); break;
                case 1: Object o = "s"; Integer i = (Integer) o; break;
                case 2: int[] a = new int[1]; a[2] = 1; break;
                case 3: System.out.println(1 / (k - 3)); break;
                case 4: throw new IllegalStateException("ise");
                case 5: "abc".substring(9); break;
                default: System.out.println(k + " none");
            }
        } catch (NumberFormatException | ClassCastException e) {
            System.out.println(k + " first group: " + e.getClass().getSimpleName() + " / " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException | ArithmeticException e) {
            System.out.println(k + " second group: " + e.getMessage());
        } catch (IllegalStateException | StringIndexOutOfBoundsException e) {
            RuntimeException r = e;
            System.out.println(k + " third group: " + r.getMessage());
        }
    }
    public static void main(String[] args) {
        for (int k = 0; k <= 6; k++) go(k);
        try {
            throw new java.io.IOException("io!");
        } catch (java.io.IOException | RuntimeException e) {
            System.out.println("effectively final multi: " + e);
        }
    }
}
