public class Main {
    public static void main(String[] args) {
        String chars = "aZ5 _\t$\néßA09-z!";
        for (char c : chars.toCharArray()) {
            String shown = c == '\t' ? "\\t" : c == '\n' ? "\\n" : String.valueOf(c);
            System.out.println("[" + shown + "] " + (int) c + " dig=" + Character.isDigit(c) + " let=" + Character.isLetter(c) + " lod=" + Character.isLetterOrDigit(c)
                + " up=" + Character.isUpperCase(c) + " low=" + Character.isLowerCase(c) + " ws=" + Character.isWhitespace(c) + " alpha=" + Character.isAlphabetic(c)
                + " num=" + Character.getNumericValue(c) + " dv=" + Character.digit(c, 10) + " jid=" + Character.isJavaIdentifierStart(c));
        }
        System.out.println(Character.getNumericValue('a') + " " + Character.getNumericValue('z') + " " + Character.getNumericValue('Z') + " " + Character.getNumericValue('9') + " " + Character.getNumericValue('-'));
        System.out.println(Character.digit('f', 16) + " " + Character.digit('F', 16) + " " + Character.digit('g', 16) + " " + Character.digit('7', 8) + " " + Character.digit('8', 8) + " " + Character.digit('z', 36));
        System.out.println(Character.forDigit(11, 16) + " " + Character.forDigit(5, 10) + " " + (int) Character.forDigit(16, 16) + " " + Character.forDigit(35, 36));
        System.out.println(Character.toUpperCase('a') + " " + Character.toLowerCase('A') + " " + Character.toUpperCase('1') + " " + Character.toUpperCase('ß') + " " + (int) Character.toUpperCase('é') + " " + Character.toUpperCase(97) + " " + Character.toChars(66)[0]);
        System.out.println(Character.toString('x') + Character.valueOf('y') + Character.toString(122) + " " + Character.valueOf('a').compareTo('d') + " " + Character.valueOf('a').equals('a') + " " + Character.isSpaceChar(' ') + " " + Character.isISOControl('\n'));
        System.out.println((int) Character.MIN_VALUE + " " + (int) Character.MAX_VALUE + " " + Character.MIN_RADIX + " " + Character.MAX_RADIX + " " + Character.isDefined('a') + " " + Character.getType('a') + " " + Character.getType('A') + " " + Character.getType('1'));
        String text = "Hello World 123 Foo_Bar!";
        int up = 0, lo = 0, di = 0, sp = 0, ot = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isUpperCase(c)) up++;
            else if (Character.isLowerCase(c)) lo++;
            else if (Character.isDigit(c)) di++;
            else if (Character.isWhitespace(c)) sp++;
            else ot++;
        }
        System.out.println(up + " " + lo + " " + di + " " + sp + " " + ot);
        StringBuilder caesar = new StringBuilder();
        for (char c : "Hello, World!".toCharArray()) {
            if (Character.isUpperCase(c)) caesar.append((char) ('A' + (c - 'A' + 3) % 26));
            else if (Character.isLowerCase(c)) caesar.append((char) ('a' + (c - 'a' + 3) % 26));
            else caesar.append(c);
        }
        System.out.println(caesar);
        int hex = 0;
        for (char c : "BEEF".toCharArray()) hex = hex * 16 + Character.digit(c, 16);
        System.out.println(hex);
        StringBuilder toggled = new StringBuilder();
        for (char c : "Hello World".toCharArray()) toggled.append(Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c));
        System.out.println(toggled);
        System.out.println(Character.isDigit('٣') + " " + Character.getNumericValue('٣') + " " + Character.isLetter('中') + " " + Character.isLetter('_') + " " + Character.isJavaIdentifierPart('$') + " " + Character.isJavaIdentifierPart('-'));
        System.out.println("café".toUpperCase() + " " + "STRASSEß".toLowerCase() + " " + "ß".toUpperCase() + " " + "i".toUpperCase() + " " + "I".toLowerCase());
        System.out.println("abc".codePointAt(1) + " " + "abc".codePoints().sum() + " " + new String(new int[]{72, 105}, 0, 2) + " " + Character.charCount(65) + " " + Character.isLetter(65));
    }
}
