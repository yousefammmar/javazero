public class Main {
    public static void main(String[] args) {
        System.out.println(Integer.parseInt("42") + Integer.parseInt("-17") + Integer.parseInt("+5"));
        System.out.println(Integer.parseInt("ff", 16) + " " + Integer.parseInt("-FF", 16) + " " + Integer.parseInt("1010", 2) + " " + Integer.parseInt("777", 8) + " " + Integer.parseInt("zz", 36) + " " + Integer.parseInt("007"));
        System.out.println(Integer.valueOf("12") + Integer.valueOf(3) + " " + Integer.decode("0x1F") + " " + Integer.decode("010") + " " + Integer.decode("-#A"));
        String[] bad = {"", " 5", "5 ", "abc", "1.5", "2147483648", "-2147483649", "0x10", "--1", null, "1_000"};
        for (String s : bad) {
            try {
                System.out.println("parsed " + Integer.parseInt(s));
            } catch (NumberFormatException e) {
                System.out.println("NFE " + e.getMessage());
            }
        }
        System.out.println(Integer.MAX_VALUE + " " + Integer.MIN_VALUE + " " + Long.MAX_VALUE + " " + Long.MIN_VALUE);
        System.out.println(Short.MAX_VALUE + " " + Short.MIN_VALUE + " " + Byte.MAX_VALUE + " " + Byte.MIN_VALUE + " " + (int) Character.MAX_VALUE);
        System.out.println(Float.MAX_VALUE + " " + Float.MIN_VALUE + " " + Double.MAX_VALUE + " " + Double.MIN_VALUE);
        System.out.println(Integer.SIZE + " " + Long.SIZE + " " + Integer.BYTES + " " + Double.BYTES + " " + Character.BYTES);
        System.out.println(Integer.toBinaryString(42) + " " + Integer.toBinaryString(-42) + " " + Integer.toHexString(255) + " " + Integer.toHexString(-255) + " " + Integer.toOctalString(64));
        System.out.println(Integer.toString(255, 2) + " " + Integer.toString(255, 36) + " " + Integer.toString(-255, 16) + " " + Long.toString(1L << 40, 16) + " " + Long.toBinaryString(5));
        System.out.println(Integer.toUnsignedString(-1) + " " + Integer.toUnsignedLong(-1) + " " + Integer.parseUnsignedInt("4294967295") + " " + Long.toUnsignedString(-1L) + " " + Integer.divideUnsigned(-1, 2) + " " + Integer.remainderUnsigned(-1, 7) + " " + Integer.compareUnsigned(-1, 1));
        System.out.println(Integer.bitCount(255) + " " + Integer.bitCount(-1) + " " + Long.bitCount(0xF0F0L) + " " + Integer.signum(-9) + " " + Integer.signum(0) + " " + Long.signum(77L));
        System.out.println(Integer.compare(3, 5) + " " + Integer.compare(5, 5) + " " + Integer.compare(9, 5) + " " + Long.compare(1, 2) + " " + Double.compare(1.5, 0.5) + " " + Character.compare('a', 'c') + " " + Boolean.compare(true, false) + " " + Integer.valueOf(5).compareTo(7));
        System.out.println(Integer.max(3, 9) + " " + Integer.min(3, 9) + " " + Integer.sum(3, 9) + " " + Long.max(1, 2) + " " + Double.max(1, 2) + " " + Double.sum(0.1, 0.2));
        System.out.println(Integer.hashCode(77) + " " + Long.hashCode(1L << 32) + " " + Double.hashCode(1.5) + " " + Boolean.hashCode(true) + " " + Boolean.hashCode(false) + " " + Character.hashCode('a') + " " + Long.valueOf(5).hashCode() + " " + "ab".hashCode());
        System.out.println(Long.parseLong("9223372036854775807") + " " + Long.parseLong("-9223372036854775808") + " " + Long.parseLong("zz", 36));
        try { Long.parseLong("9223372036854775808"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        System.out.println(Double.parseDouble("1e3") + " " + Double.valueOf("  .5 ") + " " + Double.parseDouble("Infinity") + " " + Double.parseDouble("-NaN".substring(1)) + " " + Float.parseFloat("1.1") + " " + Double.parseDouble("1d") + " " + Double.parseDouble("0x1.8p1"));
        try { Double.parseDouble("1,5"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Double.parseDouble(""); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        System.out.println(Boolean.parseBoolean("TRUE") + " " + Boolean.parseBoolean("yes") + " " + Boolean.parseBoolean(null) + " " + Boolean.valueOf("true") + " " + Boolean.logicalXor(true, false) + " " + Boolean.logicalAnd(true, false));
        System.out.println(Byte.parseByte("-128") + " " + Short.parseShort("32767") + " " + Byte.toUnsignedInt((byte) -1) + " " + Short.toUnsignedInt((short) -1));
        try { Byte.parseByte("128"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        Integer i1 = 5; int i2 = i1 + 3; long l1 = i1;
        System.out.println(i2 + " " + l1 + " " + i1.doubleValue() + " " + i1.longValue() + " " + Double.valueOf(3.99).intValue() + " " + Double.valueOf(-3.99).intValue() + " " + Long.valueOf(1L << 33).intValue() + " " + Integer.valueOf(200).byteValue());
        System.out.println(Integer.reverse(8) + " " + Integer.reverseBytes(1) + " " + Integer.highestOneBit(1000) + " " + Long.numberOfTrailingZeros(1L << 40) + " " + Integer.rotateRight(1, 1) + " " + Long.highestOneBit(1000L));
        System.out.println(Double.MAX_VALUE * 2 + " " + -Double.MAX_VALUE * 2 + " " + Double.MIN_VALUE / 2 + " " + Float.MAX_VALUE * 2 + " " + Double.POSITIVE_INFINITY + " " + Double.NEGATIVE_INFINITY + " " + Double.NaN);
        System.out.println(Double.isFinite(1.0) + " " + Double.isFinite(1 / 0.0) + " " + Float.isNaN(0f / 0) + " " + Double.toString(1.0E-4) + " " + Double.valueOf(7).equals(7.0) + " " + Integer.valueOf(7).equals(7L) + " " + Long.valueOf(7).equals(7L));
        System.out.println(Integer.toString(Integer.MIN_VALUE, 16) + " " + Integer.toHexString(Integer.MIN_VALUE) + " " + Integer.parseInt("-80000000", 16) + " " + Long.toHexString(Long.MIN_VALUE));
    }
}
