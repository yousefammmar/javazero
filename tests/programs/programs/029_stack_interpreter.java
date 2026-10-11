import java.util.*;

public class Main {
    static class VMException extends RuntimeException {
        VMException(String m) { super(m); }
    }

    static class VM {
        final Deque<Integer> stack = new ArrayDeque<>();
        final Map<String, Integer> vars = new TreeMap<>();
        final StringBuilder out = new StringBuilder();
        int steps = 0;

        int pop() {
            if (stack.isEmpty()) throw new VMException("stack underflow");
            return stack.pop();
        }

        void run(String program) {
            String[] t = program.trim().split("\\s+");
            Map<String, Integer> labels = new HashMap<>();
            for (int i = 0; i < t.length; i++) if (t[i].endsWith(":")) labels.put(t[i].substring(0, t[i].length() - 1), i);
            int pc = 0;
            while (pc < t.length) {
                if (++steps > 10000) throw new VMException("step limit");
                String w = t[pc++];
                if (w.endsWith(":")) continue;
                switch (w) {
                    case "+": stack.push(pop() + pop()); break;
                    case "*": stack.push(pop() * pop()); break;
                    case "-": { int b = pop(), a = pop(); stack.push(a - b); break; }
                    case "/": {
                        int b = pop(), a = pop();
                        if (b == 0) throw new VMException("divide by zero");
                        stack.push(a / b);
                        break;
                    }
                    case "dup": { int a = pop(); stack.push(a); stack.push(a); break; }
                    case "swap": { int b = pop(), a = pop(); stack.push(b); stack.push(a); break; }
                    case "drop": pop(); break;
                    case "print": out.append(pop()).append(' '); break;
                    case "store": { String name = t[pc++]; vars.put(name, pop()); break; }
                    case "load": { String name = t[pc++]; stack.push(vars.getOrDefault(name, 0)); break; }
                    case "jmp": pc = labels.get(t[pc]); break;
                    case "jz": { String l = t[pc++]; if (pop() == 0) pc = labels.get(l); break; }
                    default:
                        try { stack.push(Integer.parseInt(w)); }
                        catch (NumberFormatException e) { throw new VMException("unknown word: " + w); }
                }
            }
        }
    }

    static void exec(String name, String prog) {
        VM vm = new VM();
        try {
            vm.run(prog);
            System.out.println(name + ": out=[" + vm.out.toString().trim() + "] stack=" + vm.stack + " vars=" + vm.vars + " steps=" + vm.steps);
        } catch (VMException e) {
            System.out.println(name + ": ERROR " + e.getMessage() + " (stack=" + vm.stack + ")");
        }
    }

    public static void main(String[] args) {
        exec("arith", "2 3 + 4 * print");
        exec("swap", "1 2 swap - print");
        exec("countdown", "5 store n loop: load n jz end load n print load n 1 - store n jmp loop end:");
        exec("factorial", "1 store acc 6 store n top: load n jz done load acc load n * store acc load n 1 - store n jmp top done: load acc print");
        exec("underflow", "1 +");
        exec("divzero", "4 0 /");
        exec("badword", "1 2 frobnicate");
        exec("infinite", "x: jmp x");
        exec("dup", "7 dup * dup print print");
    }
}
