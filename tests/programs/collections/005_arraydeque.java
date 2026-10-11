import java.util.*;
public class Main {
    public static void main(String[] args) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 1; i <= 5; i++) stack.push(i);
        System.out.println("stack " + stack);
        System.out.println("peek " + stack.peek() + " pop " + stack.pop() + " " + stack);
        Queue<String> q = new ArrayDeque<>();
        q.offer("a"); q.add("b"); q.offer("c");
        System.out.println(q + " peek=" + q.peek() + " poll=" + q.poll() + " " + q);
        System.out.println("element " + q.element() + " remove " + q.remove());
        System.out.println(q.poll() + " " + q.poll() + " " + q.peek());
        try { q.remove(); } catch (NoSuchElementException e) { System.out.println("NSEE remove"); }
        try { q.element(); } catch (NoSuchElementException e) { System.out.println("NSEE element"); }
        try { stack.addFirst(null); } catch (NullPointerException e) { System.out.println("NPE null"); }
        Deque<Integer> d = new ArrayDeque<>();
        for (int i = 0; i < 6; i++) { d.offerFirst(i); d.offerLast(-i); }
        System.out.println(d);
        Iterator<Integer> di = d.descendingIterator();
        StringBuilder sb = new StringBuilder();
        while (di.hasNext()) sb.append(di.next()).append(' ');
        System.out.println(sb.toString().trim());
        System.out.println(d.pollFirst() + " " + d.pollLast() + " " + d.peekFirst() + " " + d.peekLast() + " size " + d.size());
        d.removeFirstOccurrence(0);
        System.out.println(d + " contains 3 " + d.contains(3));
        for (int x : d) if (x > 2) System.out.print(x + ";");
        System.out.println();
        // balanced parens with stack
        String[] tests = {"(a[b]{c})", "(]", "((", "{[()]}", ""};
        for (String t : tests) {
            Deque<Character> st = new ArrayDeque<>();
            boolean ok = true;
            for (char c : t.toCharArray()) {
                if ("([{".indexOf(c) >= 0) st.push(c);
                else if (")]}".indexOf(c) >= 0) {
                    if (st.isEmpty()) { ok = false; break; }
                    char o = st.pop();
                    if ("([{".indexOf(o) != ")]}".indexOf(c)) { ok = false; break; }
                }
            }
            System.out.println("'" + t + "' balanced=" + (ok && st.isEmpty()));
        }
    }
}
