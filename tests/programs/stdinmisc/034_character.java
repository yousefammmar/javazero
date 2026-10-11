public class Main {
    public static void main(String[] args) {
        int[] cps = {0, 9, 10, 32, 33, 48, 57, 65, 90, 95, 97, 122, 126, 127, 160, 169, 181, 223, 228, 233, 255, 256, 937, 945, 1040, 8364, 12354, 65535};
        for (int cp : cps) {
            char c = (char) cp;
            System.out.println(cp + " L" + b(Character.isLetter(c)) + " D" + b(Character.isDigit(c)) + " A" + b(Character.isLetterOrDigit(c)) + " U" + b(Character.isUpperCase(c)) + " l" + b(Character.isLowerCase(c)) + " W" + b(Character.isWhitespace(c)) + " S" + b(Character.isSpaceChar(c)) + " up=" + (int) Character.toUpperCase(c) + " lo=" + (int) Character.toLowerCase(c) + " type=" + Character.getType(c));
        }
        System.out.println(Character.getNumericValue('7') + " " + Character.getNumericValue('a') + " " + Character.getNumericValue('Z') + " " + Character.getNumericValue('-'));
        System.out.println(Character.digit('f', 16) + " " + Character.digit('9', 8) + " " + Character.forDigit(11, 16) + " " + Character.forDigit(3, 10));
        System.out.println(Character.toString(97) + Character.valueOf('b') + Character.toChars(99)[0]);
        System.out.println((char) ('a' + 1) + " " + ('a' + 1) + " " + (char) 66 + " " + (int) 'A' + " " + ('a' < 'b'));
        char c = 'x'; c += 1; c++;
        System.out.println(c);
        System.out.println(Character.MAX_VALUE + 0);
        System.out.println(Character.compare('a', 'c') + " " + Character.isAlphabetic('e') + " " + Character.isJavaIdentifierStart('$'));
        System.out.println(Character.toUpperCase('ß') + " " + "straße".toUpperCase() + " " + "İ".toLowerCase().length());
    }
    static int b(boolean x) { return x ? 1 : 0; }
}
