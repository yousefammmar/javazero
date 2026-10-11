public class Main {
    public static void main(String[] args) {
        System.out.println(String.format("[%08.3f] [%+d] [%,.2f] [%(d] [%S]", 3.14159, 5, 1234567.891, -42, "hello"));
        System.out.println(String.format("[%10.4s] [%-6b] [%6b] [%x] [%X] [%o]", "abcdefghij", true, false, -255, 255, -8));
        System.out.println(String.format("[%c] [%c] [%5c] [%-5c]|", 65, 'z', 'y', 'x'));
        System.out.println(String.format("[%e] [%.2e] [%E] [%g] [%.3g]", 12345.6789, 0.000123, 1e10, 0.0001234, 123456.0));
        System.out.println(String.format("[%5d] [%-5d] [%05d] [%,d] [% d] [%+d]", 42, 42, 42, -1234567, 42, 0));
        System.out.println(String.format("[%10s] [%-10s] [%.2s] [%s] [%s]", "hi", "hi", "hello", null, 1.0f));
        System.out.println(String.format("%d%%  %n", 50).length());
        System.out.println(String.format("[%b] [%b] [%b]", null, "x", 0));
        System.out.println(String.format("[%d] [%d] [%d]", (byte) -1, (short) 300, Long.MIN_VALUE));
        System.out.println(String.format("[%x] [%x] [%08X] [%#x] [%#o]", 255L, -1L, 48879, 255, 8));
        System.out.println(String.format("%2$s %1$s %2$s %<s", "a", "b"));
        System.out.println(String.format("[%.0f] [%.1f] [%10.3f] [%-10.1f] [%f]", 2.5, 0.05, Math.PI, Math.E, 1.0));
        System.out.println(String.format("[%s] [%s] [%s]", new int[0].length, 'c', 10L));
        System.out.printf("%s has %d items costing %.2f%n", "cart", 3, 9.999);
        System.out.printf("%5.1f%%%n", 45.678);
        System.out.println(String.format("[%,d] [%,d] [%,.3f]", 999, 1000, 0.5));
        System.out.println(String.format("|%-8s|%8s|", "ab", "cd"));
        System.out.println(String.format("%b %c %d", true, 'q', 7));
    }
}
