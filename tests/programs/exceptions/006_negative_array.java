public class Main {
    static int[] make(int n) { return new int[n]; }
    public static void main(String[] args) {
        int[] sizes = {3, 0, -1, -100, Integer.MIN_VALUE};
        for (int n : sizes) {
            try {
                int[] a = make(n);
                System.out.println("made length " + a.length);
            } catch (NegativeArraySizeException e) {
                System.out.println("msg=" + e.getMessage());
                System.out.println(e);
                System.out.println(e.getClass().getName());
                System.out.println(e instanceof RuntimeException);
            }
        }
        try { String[][] g = new String[2][-3]; System.out.println(g.length); }
        catch (NegativeArraySizeException e) { System.out.println("2d: " + e.getMessage()); }
        try { long[] l = new long[-7]; }
        catch (NegativeArraySizeException e) { System.out.println("long: " + e.getMessage()); }
        try { Object[] o = new Object[1 - 2]; }
        catch (RuntimeException e) { System.out.println(e.getClass().getSimpleName() + " " + e.getMessage()); }
        try { java.util.Arrays.copyOf(new int[2], -1); }
        catch (NegativeArraySizeException e) { System.out.println("copyOf: " + e.getMessage()); }
        try { new java.util.ArrayList<Integer>(-2); }
        catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        try { new StringBuilder(-1); }
        catch (NegativeArraySizeException e) { System.out.println("sb: " + e.getMessage()); }
    }
}
