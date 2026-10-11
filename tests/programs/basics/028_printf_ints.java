public class Main {
    public static void main(String[] args) {
        System.out.printf("[%d]%n", 42);
        System.out.printf("[%5d]%n", 42);
        System.out.printf("[%-5d]%n", 42);
        System.out.printf("[%05d]%n", 42);
        System.out.printf("[%+d] [%+d]%n", 42, -42);
        System.out.printf("[% d] [% d]%n", 42, -42);
        System.out.printf("[%,d]%n", 1234567);
        System.out.printf("[%,d]%n", -1234567);
        System.out.printf("[%,d]%n", 999);
        System.out.printf("[%,15d]%n", 1234567890L);
        System.out.printf("[%(d]%n", -5);
        System.out.printf("[%05d]%n", -42);
        System.out.printf("[%3d]%n", 123456);
        System.out.printf("[%d] [%d]%n", Integer.MIN_VALUE, Long.MAX_VALUE);
        System.out.printf("[%d]%n", (byte) -3);
        System.out.printf("[%d]%n", (short) 300);
        System.out.printf("[%x] [%X] [%#x] [%#X]%n", 255, 255, 255, 255);
        System.out.printf("[%x]%n", -1);
        System.out.printf("[%x]%n", -1L);
        System.out.printf("[%x]%n", (byte) -1);
        System.out.printf("[%08x]%n", 48879);
        System.out.printf("[%o] [%#o]%n", 64, 64);
        System.out.printf("[%o]%n", -1);
        System.out.printf("[%10x]%n", 4096);
        System.out.printf("[%-10x]%n", 4096);
        System.out.printf("[%c] [%c] [%C]%n", 'a', 98, 'z');
        System.out.printf("[%5c] [%-5c]%n", 'q', 'r');
        System.out.printf("[%b] [%b] [%b] [%b]%n", true, false, null, "x");
        System.out.printf("[%B] [%6b] [%-6b]%n", true, false, true);
        System.out.printf("[%s] [%S]%n", "text", "text");
        System.out.printf("[%s %s %s %s %s]%n", 1, 2.5, 'c', true, null);
        System.out.printf("[%10s] [%-10s]%n", "right", "left");
        System.out.printf("[%.3s] [%10.3s] [%-10.2s]%n", "abcdef", "abcdef", "abcdef");
        System.out.printf("100%% done%n");
        System.out.printf("[%%] [%5%] [%-5%]%n");
        System.out.printf("%d%s%c%n", 1, "two", '3');
        System.out.printf("%2$s %1$s %2$s%n", "a", "b");
        System.out.printf("%1$d %1$x %1$o %1$c%n", 65);
        System.out.printf("%s %<s %<S%n", "rep");
        System.out.printf("%-6s|%6s|%n", "ab", "cd");
        System.out.printf("%s%n", new int[0].length);
        System.out.printf("%s%n", java.util.List.of(1, 2));
        System.out.printf("%s%n", (Object) null);
        System.out.printf("%d items%n", java.util.List.of(1, 2).size());
        System.out.printf("%b%n", 0);
        String f = String.format("%03d|%3d|%-3d|", 7, 7, 7);
        System.out.println(f + f.length());
        System.out.println(String.format("%s=%d", "x", 10) + String.format("%n").length());
        System.out.println(String.format("|%8s|%8s|", Integer.toBinaryString(5), Integer.toHexString(255)));
        System.out.println(String.format("%08d", Integer.parseInt(Integer.toBinaryString(37))));
        System.out.println(String.format("%,d", Long.MIN_VALUE));
        System.out.println(String.format("%d", new java.math.BigInteger("123456789012345678901234567890")));
        for (int i = 1; i <= 5; i++) System.out.println(String.format("%2d x %2d = %3d", i, i + 5, i * (i + 5)));
        try {
            System.out.printf("%d%n", "str");
        } catch (java.util.IllegalFormatConversionException e) {
            System.out.println("IFCE " + e.getMessage());
        }
        try {
            System.out.printf("%d %d%n", 1);
        } catch (java.util.MissingFormatArgumentException e) {
            System.out.println("MFAE " + e.getMessage());
        }
        try {
            System.out.printf("%q%n", 1);
        } catch (java.util.UnknownFormatConversionException e) {
            System.out.println("UFCE " + e.getMessage());
        }
        try {
            System.out.printf("%d%n", 1.5);
        } catch (java.util.IllegalFormatException e) {
            System.out.println("IFE " + e.getMessage());
        }
    }
}
