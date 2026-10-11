public class Main {
    static void p(String s) {
        try { System.out.println("parseInt(" + s + ") = " + Integer.parseInt(s)); }
        catch (NumberFormatException e) { System.out.println(e.getMessage() + " | " + e); }
    }
    public static void main(String[] args) {
        String[] in = {"abc", "", " 12", "12 ", "12.5", "99999999999", "-99999999999", "+5", "-", "+", "0x10", "1_000", "2147483647", "2147483648", "-2147483648", "١٢"};
        for (String s : in) p(s);
        p(null);
        try { Integer.parseInt("zz", 36); System.out.println(Integer.parseInt("zz", 36)); Integer.parseInt("9", 8); }
        catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Integer.parseInt("1", 99); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Integer.valueOf("x1"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Long.parseLong("12L"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Long.parseLong("9223372036854775808"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Long.parseLong(""); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Long.parseLong(null); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Double.parseDouble("abc"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Double.parseDouble(""); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Double.parseDouble("1.2.3"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Double.parseDouble(null); } catch (NullPointerException e) { System.out.println("NPE from parseDouble(null)"); }
        System.out.println(Double.parseDouble(" 3.5 ") + " " + Double.parseDouble("1e3") + " " + Double.parseDouble("NaN") + " " + Double.parseDouble("-Infinity"));
        try { Boolean.parseBoolean("maybe"); System.out.println(Boolean.parseBoolean("maybe")); } catch (Exception e) { System.out.println("no"); }
        try { Byte.parseByte("200"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Short.parseShort("70000"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Integer.decode("0xZZ"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        System.out.println(e1().getClass().getSuperclass().getName());
    }
    static NumberFormatException e1() { return new NumberFormatException("x"); }
}
