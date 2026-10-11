import java.util.*;
public class Main {
    public static void main(String[] args) {
        Stack<Integer> s = new Stack<>();
        System.out.println("empty " + s.empty() + " " + s);
        for (int i = 1; i <= 5; i++) System.out.println("push returns " + s.push(i * i));
        System.out.println(s + " size " + s.size());
        System.out.println("peek " + s.peek() + " search 9=" + s.search(9) + " search 1=" + s.search(1) + " search 7=" + s.search(7));
        System.out.println("pop " + s.pop() + " " + s.pop());
        System.out.println(s + " get(0)=" + s.get(0) + " firstElement=" + s.firstElement() + " lastElement=" + s.lastElement());
        s.add(0, 100);
        System.out.println(s + " indexOf 4: " + s.indexOf(4));
        while (!s.isEmpty()) System.out.print(s.pop() + " ");
        System.out.println();
        try { s.pop(); } catch (EmptyStackException e) { System.out.println("EmptyStackException " + e.getMessage()); }
        try { s.peek(); } catch (EmptyStackException e) { System.out.println("peek empty"); }
        // RPN evaluator
        String[] exprs = {"3 4 + 2 *", "5 1 2 + 4 * + 3 -", "10 2 8 * + 3 -"};
        for (String e : exprs) {
            Stack<Integer> st = new Stack<>();
            for (String tok : e.split(" ")) {
                switch (tok) {
                    case "+": st.push(st.pop() + st.pop()); break;
                    case "*": st.push(st.pop() * st.pop()); break;
                    case "-": { int b = st.pop(), a = st.pop(); st.push(a - b); break; }
                    default: st.push(Integer.parseInt(tok));
                }
            }
            System.out.println(e + " = " + st.pop());
        }
        Stack<String> words = new Stack<>();
        for (String w : "to be or not".split(" ")) words.push(w);
        StringBuilder sb = new StringBuilder();
        for (String w : words) sb.append(w).append(',');
        System.out.println("iteration (bottom first): " + sb);
        System.out.println(words.subList(1, 3));
    }
}
