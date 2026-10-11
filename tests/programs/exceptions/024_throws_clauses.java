import java.io.*;
public class Main {
    interface Loader { String load(String k) throws IOException; }
    static class Base { void run() throws IOException { throw new FileNotFoundException("base"); } }
    static class Derived extends Base {
        @Override void run() throws FileNotFoundException { throw new FileNotFoundException("derived"); }
    }
    static class Quiet extends Base { @Override void run() { System.out.println("quiet run"); } }
    static String load(String k) throws IOException, InterruptedException {
        if (k.equals("io")) throw new IOException("disk");
        if (k.equals("int")) throw new InterruptedException("sleep");
        if (k.equals("rt")) throw new IllegalArgumentException("rt");
        return "loaded " + k;
    }
    static <T extends Exception> void sneaky(Exception e) throws T { @SuppressWarnings("unchecked") T t = (T) e; throw t; }
    public static void main(String[] args) throws Exception {
        for (String k : new String[]{"ok", "io", "int", "rt"}) {
            try { System.out.println(load(k)); }
            catch (IOException | InterruptedException e) { System.out.println("checked " + e); }
            catch (RuntimeException e) { System.out.println("unchecked " + e); }
        }
        Loader l = key -> { if (key.isEmpty()) throw new IOException("empty key"); return key.toUpperCase(); };
        try { System.out.println(l.load("abc")); System.out.println(l.load("")); }
        catch (IOException e) { System.out.println(e.getMessage()); }
        Base[] bs = {new Base(), new Derived(), new Quiet()};
        for (Base b : bs) {
            try { b.run(); } catch (FileNotFoundException e) { System.out.println("FNF " + e.getMessage()); }
            catch (IOException e) { System.out.println("IO " + e.getMessage()); }
        }
        try { Main.<RuntimeException>sneaky(new IOException("sneaked")); }
        catch (Exception e) { System.out.println(e.getClass().getName() + " " + e.getMessage()); }
        Thread t = new Thread(() -> { throw new IllegalStateException("never printed since join first"); });
        System.out.println("main throws clause ok");
    }
}
