import java.util.*;

public class Main {
    enum Kind { LBRACE, RBRACE, LBRACK, RBRACK, COLON, COMMA, STRING, NUMBER, LITERAL }

    record Token(Kind kind, String text) {}

    static List<Token> tokenize(String s) {
        List<Token> out = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            switch (c) {
                case '{' -> { out.add(new Token(Kind.LBRACE, "{")); i++; }
                case '}' -> { out.add(new Token(Kind.RBRACE, "}")); i++; }
                case '[' -> { out.add(new Token(Kind.LBRACK, "[")); i++; }
                case ']' -> { out.add(new Token(Kind.RBRACK, "]")); i++; }
                case ':' -> { out.add(new Token(Kind.COLON, ":")); i++; }
                case ',' -> { out.add(new Token(Kind.COMMA, ",")); i++; }
                case '"' -> {
                    int j = i + 1;
                    while (s.charAt(j) != '"') j++;
                    out.add(new Token(Kind.STRING, s.substring(i + 1, j)));
                    i = j + 1;
                }
                default -> {
                    if (Character.isWhitespace(c)) { i++; break; }
                    int j = i;
                    while (j < s.length() && ",]} \t\n".indexOf(s.charAt(j)) < 0) j++;
                    String w = s.substring(i, j);
                    boolean num = Character.isDigit(w.charAt(0)) || w.charAt(0) == '-';
                    out.add(new Token(num ? Kind.NUMBER : Kind.LITERAL, w));
                    i = j;
                }
            }
        }
        return out;
    }

    static String pretty(List<Token> toks) {
        StringBuilder sb = new StringBuilder();
        int indent = 0;
        for (int i = 0; i < toks.size(); i++) {
            Token t = toks.get(i);
            switch (t.kind()) {
                case LBRACE, LBRACK -> {
                    sb.append(t.text());
                    Kind next = toks.get(i + 1).kind();
                    if (next == Kind.RBRACE || next == Kind.RBRACK) break;
                    indent++;
                    sb.append('\n').append("  ".repeat(indent));
                }
                case RBRACE, RBRACK -> {
                    Kind prev = toks.get(i - 1).kind();
                    if (prev != Kind.LBRACE && prev != Kind.LBRACK) {
                        indent--;
                        sb.append('\n').append("  ".repeat(indent));
                    }
                    sb.append(t.text());
                }
                case COMMA -> sb.append(",\n").append("  ".repeat(indent));
                case COLON -> sb.append(": ");
                case STRING -> sb.append('"').append(t.text()).append('"');
                default -> sb.append(t.text());
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String json = "{\"name\":\"Ada\",\"age\":36,\"langs\":[\"en\",\"fr\"],\"address\":{\"city\":\"London\",\"zip\":null},\"empty\":[],\"ok\":true,\"pi\":-3.14}";
        List<Token> toks = tokenize(json);
        System.out.println(toks.size() + " tokens");
        Map<Kind, Integer> counts = new EnumMap<>(Kind.class);
        for (Token t : toks) counts.merge(t.kind(), 1, Integer::sum);
        System.out.println(counts);
        System.out.println(toks.subList(0, 6));
        System.out.println(pretty(toks));
        System.out.println(pretty(tokenize("[1, [2, [3, {}]], \"x\"]")));
    }
}
