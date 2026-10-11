public class Main {
    static void check(int kind) {
        switch (kind) {
            case 0: throw new IllegalArgumentException("bad arg");
            case 1: throw new IllegalStateException("bad state");
            case 2: throw new UnsupportedOperationException("nope");
            case 3: throw new IllegalArgumentException();
            case 4: throw new RuntimeException();
            case 5: throw new IllegalStateException(new RuntimeException("inner"));
            case 6: throw new UnsupportedOperationException();
            case 7: throw new ArithmeticException("custom arith");
            case 8: throw new IndexOutOfBoundsException("idx");
            default: System.out.println("kind " + kind + " fine");
        }
    }
    public static void main(String[] args) {
        for (int i = 0; i <= 9; i++) {
            try { check(i); }
            catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("A " + e + " msg=" + e.getMessage() + " cause=" + e.getCause());
            } catch (UnsupportedOperationException e) {
                System.out.println("B " + e.getClass().getSimpleName() + " " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("C " + e.getClass().getName() + " " + e.getMessage());
            }
        }
        Exception ex = new Exception();
        System.out.println(ex + " " + ex.getMessage() + " " + ex.getLocalizedMessage());
        Throwable th = new Throwable("t");
        System.out.println(th);
        Error er = new Error("err", new Exception("why"));
        System.out.println(er + " / " + er.getCause());
        System.out.println(new Exception((String) null).getMessage());
        System.out.println(new Exception((Throwable) null).getMessage());
    }
}
