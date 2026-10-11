public class Main {
    static class BaseEx extends Exception { BaseEx(String m) { super(m); } }
    static class MidEx extends BaseEx { MidEx(String m) { super("mid:" + m); } }
    static class LeafEx extends MidEx { LeafEx(String m) { super("leaf:" + m); } }
    static class OtherEx extends BaseEx { OtherEx(String m) { super("other:" + m); } }
    static void raise(int k) throws BaseEx {
        switch (k) {
            case 0: throw new LeafEx("a");
            case 1: throw new MidEx("b");
            case 2: throw new OtherEx("c");
            case 3: throw new BaseEx("d");
        }
    }
    static String classify(int k) {
        try { raise(k); return "none"; }
        catch (LeafEx e) { return "Leaf(" + e.getMessage() + ")"; }
        catch (MidEx e) { return "Mid(" + e.getMessage() + ")"; }
        catch (OtherEx e) { return "Other(" + e.getMessage() + ")"; }
        catch (BaseEx e) { return "Base(" + e.getMessage() + ")"; }
    }
    public static void main(String[] args) {
        for (int k = 0; k < 5; k++) System.out.println(k + " -> " + classify(k));
        try { raise(0); } catch (BaseEx e) {
            System.out.println(e.getClass().getName());
            System.out.println(e instanceof MidEx);
            System.out.println(e instanceof OtherEx);
            Class<?> c = e.getClass();
            while (c != null) { System.out.println("  " + c.getSimpleName()); c = c.getSuperclass(); }
        }
        try { raise(1); } catch (LeafEx e) { System.out.println("wrong"); } catch (Exception e) { System.out.println("general " + e.getMessage()); }
        try { raise(2); } catch (MidEx | OtherEx e) { System.out.println("multi " + e.getMessage()); } catch (BaseEx e) { System.out.println("no"); }
    }
}
