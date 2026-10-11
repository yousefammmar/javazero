import java.util.*;

public class Main {
    static String src;
    static int pos;

    static class ParseException extends RuntimeException {
        ParseException(String m) { super(m); }
    }

    static double evaluate(String s) {
        src = s.replace(" ", "");
        pos = 0;
        double v = expr();
        if (pos != src.length()) throw new ParseException("Unexpected '" + src.charAt(pos) + "' at " + pos);
        return v;
    }

    static double expr() {
        double v = term();
        while (pos < src.length() && (src.charAt(pos) == '+' || src.charAt(pos) == '-')) {
            char op = src.charAt(pos++);
            double r = term();
            v = op == '+' ? v + r : v - r;
        }
        return v;
    }

    static double term() {
        double v = factor();
        while (pos < src.length() && (src.charAt(pos) == '*' || src.charAt(pos) == '/' || src.charAt(pos) == '%')) {
            char op = src.charAt(pos++);
            double r = factor();
            if (op == '*') v *= r;
            else if (op == '/') {
                if (r == 0) throw new ArithmeticException("division by zero");
                v /= r;
            } else v %= r;
        }
        return v;
    }

    static double factor() {
        double base = unary();
        if (pos < src.length() && src.charAt(pos) == '^') {
            pos++;
            return Math.pow(base, factor());
        }
        return base;
    }

    static double unary() {
        if (pos < src.length() && src.charAt(pos) == '-') { pos++; return -unary(); }
        return primary();
    }

    static double primary() {
        if (pos >= src.length()) throw new ParseException("Unexpected end");
        char c = src.charAt(pos);
        if (c == '(') {
            pos++;
            double v = expr();
            if (pos >= src.length() || src.charAt(pos) != ')') throw new ParseException("Missing )");
            pos++;
            return v;
        }
        int start = pos;
        while (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.')) pos++;
        if (start == pos) throw new ParseException("Unexpected '" + c + "' at " + pos);
        return Double.parseDouble(src.substring(start, pos));
    }

    public static void main(String[] args) {
        String[] tests = {
            "1 + 2 * 3", "(1 + 2) * 3", "10 / 4", "2 ^ 3 ^ 2", "-(3 + 4) * 2", "7 % 4 + 0.5",
            "((2))", "100 - 1 - 1 - 1", "8 / 2 / 2", "3 * (4 + 5) - 6 / (1 + 1)", "1 / 0", "2 +", "(3 + 4", "4 $ 4", "--5"
        };
        for (String t : tests) {
            try {
                double r = evaluate(t);
                System.out.println(t + " = " + r);
            } catch (ParseException e) {
                System.out.println(t + " -> parse error: " + e.getMessage());
            } catch (ArithmeticException e) {
                System.out.println(t + " -> math error: " + e.getMessage());
            }
        }
    }
}
