import java.util.*;

public class Main {
    static String check(String s) {
        Deque<int[]> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '[' || c == '{') stack.push(new int[]{c, i});
            else if (c == ')' || c == ']' || c == '}') {
                if (stack.isEmpty()) return "unmatched '" + c + "' at " + i;
                int[] top = stack.pop();
                char open = (char) top[0];
                boolean ok = (open == '(' && c == ')') || (open == '[' && c == ']') || (open == '{' && c == '}');
                if (!ok) return "mismatch '" + open + "' at " + top[1] + " vs '" + c + "' at " + i;
            }
        }
        if (!stack.isEmpty()) {
            int[] top = stack.peek();
            return "unclosed '" + (char) top[0] + "' at " + top[1];
        }
        return "balanced";
    }

    static int maxDepth(String s) {
        int d = 0, best = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') best = Math.max(best, ++d);
            else if (c == ')') d--;
        }
        return best;
    }

    static int longestValid(String s) {
        Stack<Integer> st = new Stack<>();
        st.push(-1);
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') st.push(i);
            else {
                st.pop();
                if (st.isEmpty()) st.push(i);
                else best = Math.max(best, i - st.peek());
            }
        }
        return best;
    }

    static List<String> generate(int n) {
        List<String> out = new ArrayList<>();
        gen("", 0, 0, n, out);
        return out;
    }

    static void gen(String cur, int open, int close, int n, List<String> out) {
        if (cur.length() == 2 * n) { out.add(cur); return; }
        if (open < n) gen(cur + "(", open + 1, close, n, out);
        if (close < open) gen(cur + ")", open, close + 1, n, out);
    }

    public static void main(String[] args) {
        String[] tests = {"()", "([]{})", "((()))", "(]", "((", "())", "{[(])}", "a(b)c[d]{e}", "", "}{"};
        for (String t : tests) System.out.printf("%-12s -> %s%n", "\"" + t + "\"", check(t));
        System.out.println("depth: " + maxDepth("(a(b(c))d)(e)"));
        System.out.println("longest valid: " + longestValid(")()())") + " " + longestValid("(()(((()") + " " + longestValid("()(())"));
        List<String> g = generate(3);
        System.out.println(g.size() + " " + g);
        System.out.println(generate(4).size());
        Stack<Character> s = new Stack<>();
        for (char c : "stack".toCharArray()) s.push(c);
        System.out.println(s + " search(s)=" + s.search('s') + " search(k)=" + s.search('k'));
        StringBuilder rev = new StringBuilder();
        while (!s.empty()) rev.append(s.pop());
        System.out.println(rev);
    }
}
