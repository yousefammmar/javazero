import java.util.*;
public class Main {
    static int depth = 0;
    static void recurse() { depth++; recurse(); }
    static int sum(int n) { return n == 0 ? 0 : n + sum(n - 1); }
    public static void main(String[] args) {
        Stack<Integer> st = new Stack<>();
        try { st.pop(); } catch (EmptyStackException e) { System.out.println(e + " msg=" + e.getMessage()); }
        try { st.peek(); } catch (EmptyStackException e) { System.out.println(e.getClass().getName() + " " + e.getMessage()); }
        st.push(1); st.push(2);
        System.out.println(st.pop() + " " + st.peek() + " " + st.size() + " " + st.empty());
        try { st.pop(); st.pop(); } catch (RuntimeException e) { System.out.println("caught " + e.getClass().getSimpleName()); }
        try { recurse(); }
        catch (StackOverflowError e) { System.out.println("caught StackOverflowError " + (depth > 500) + " " + (e.getMessage() == null)); }
        depth = 0;
        try { recurse(); }
        catch (Throwable t) { System.out.println(t instanceof Error); System.out.println(t instanceof VirtualMachineError); System.out.println(t.getClass().getName()); }
        System.out.println(sum(100));
        try { sum(-1); } catch (StackOverflowError e) { System.out.println("sum overflowed"); }
        Object o = new Object() { int f(int x) { return f(x + 1) + 1; } };
        System.out.println("still running");
        try { new Main().inst(1); } catch (StackOverflowError e) { System.out.println("inst overflow"); }
        try { mutualA(0); } catch (StackOverflowError e) { System.out.println("mutual overflow"); }
        try { recWithFinally(); } catch (StackOverflowError e) { System.out.println("finally overflow"); }
        System.out.println("done");
    }
    void inst(int x) { inst(x + 1); }
    static void mutualA(int x) { mutualB(x + 1); }
    static void mutualB(int x) { mutualA(x + 1); }
    static int fin = 0;
    static void recWithFinally() { try { recWithFinally(); } finally { fin++; } }
}
