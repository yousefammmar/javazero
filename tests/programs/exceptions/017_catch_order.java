import java.io.*;
import java.util.*;
public class Main {
    static void thrower(int k) throws Exception {
        switch (k) {
            case 0: throw new FileNotFoundException("fnf");
            case 1: throw new IOException("io");
            case 2: throw new UncheckedIOException(new IOException("wrapped"));
            case 3: throw new ArrayIndexOutOfBoundsException(7);
            case 4: throw new NumberFormatException("nfe");
            case 5: throw new Exception("generic");
            case 6: throw new InterruptedException("intr");
            case 7: throw new CloneNotSupportedException("clone");
            case 8: throw new NoSuchElementException("nse");
            case 9: throw new InputMismatchException("im");
        }
    }
    public static void main(String[] args) {
        for (int k = 0; k <= 10; k++) {
            try { thrower(k); System.out.println(k + " no throw"); }
            catch (FileNotFoundException e) { System.out.println(k + " FNF " + e.getMessage()); }
            catch (IOException e) { System.out.println(k + " IO " + e.getMessage()); }
            catch (ArrayIndexOutOfBoundsException e) { System.out.println(k + " AIOOBE " + e.getMessage()); }
            catch (IndexOutOfBoundsException e) { System.out.println(k + " IOOBE"); }
            catch (NumberFormatException e) { System.out.println(k + " NFE"); }
            catch (IllegalArgumentException e) { System.out.println(k + " IAE"); }
            catch (InputMismatchException e) { System.out.println(k + " IME " + e.getMessage()); }
            catch (NoSuchElementException e) { System.out.println(k + " NSE"); }
            catch (RuntimeException e) { System.out.println(k + " RTE " + e.getClass().getName() + " cause=" + e.getCause()); }
            catch (InterruptedException | CloneNotSupportedException e) { System.out.println(k + " multi " + e.getClass().getSimpleName()); }
            catch (Exception e) { System.out.println(k + " EX " + e.getMessage()); }
        }
        System.out.println(new FileNotFoundException().getMessage());
        System.out.println(new UncheckedIOException(new IOException("zz")).getMessage());
        System.out.println(new IOException(new RuntimeException("q")).getMessage());
    }
}
