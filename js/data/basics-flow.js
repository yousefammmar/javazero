/* Basics module "Decisions, Loops & Arrays": the W3Schools Java Tutorial topics between Strings and Methods
   (Math, Booleans, If...Else, Switch, While, For, Break/Continue, Arrays). Inserted right after "vars". */
MODULES.splice(1, 0,
  { id: 'flow', track: 'logic', tag: 'Logic & Scope', icon: 'branch', min: 50, name: 'Decisions, Loops & Arrays',
    desc: 'Make programs choose and repeat: if/else, switch, loops and arrays, plus the Math class.',
    lessons: [
      L('flow',1,'if, else if and else','Run different code depending on a condition.',`public class Main {
    public static void main(String[] args) {
        int score = 72;
        if (score >= 90) {
            System.out.println("A");
        } else if (score >= 70) {
            System.out.println("B");
        } else {
            System.out.println("Keep going");
        }
        boolean raining = false;
        System.out.println(raining ? "umbrella" : "sunglasses");
    }
}`,['Conditions are checked from the top and only the first true branch runs.','Curly braces group the lines that belong to a branch.','condition ? a : b is a one-line shortcut that picks one of two values.']),
      L('flow',2,'Booleans and logical operators','Combine true and false values.',`public class Main {
    public static void main(String[] args) {
        int age = 20;
        boolean student = true;
        System.out.println(age >= 18 && student);
        System.out.println(age < 18 || student);
        System.out.println(!student);
        System.out.println(age == 20);
        System.out.println(age != 20);
    }
}`,['&& means both must be true, || means at least one, and ! flips a value.','Comparison operators (==, !=, <, >, <=, >=) produce a boolean.','Use == to compare numbers, but .equals() to compare Strings.']),
      L('flow',3,'switch','Pick one branch from many fixed values.',`public class Main {
    public static void main(String[] args) {
        int day = 3;
        String name;
        switch (day) {
            case 1: name = "Monday"; break;
            case 2: name = "Tuesday"; break;
            case 3: name = "Wednesday"; break;
            default: name = "Another day";
        }
        System.out.println(name);
        String kind = switch (day) {
            case 6, 7 -> "weekend";
            default -> "weekday";
        };
        System.out.println(kind);
    }
}`,['Classic switch needs break, or execution falls through to the next case.','default runs when no case matches.','The modern arrow form (->) has no fall-through and can return a value.']),
      L('flow',4,'while and do/while','Repeat while a condition stays true.',`public class Main {
    public static void main(String[] args) {
        int n = 1;
        while (n <= 3) {
            System.out.println("while " + n);
            n++;
        }
        int m = 10;
        do {
            System.out.println("do/while " + m);
            m++;
        } while (m < 10);
    }
}`,['while checks the condition before each round, so it may run zero times.','do/while checks after the body, so it always runs at least once.','Change something inside the loop, or it never ends.']),
      L('flow',5,'for loops','Count with a loop in one line.',`public class Main {
    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) {
            System.out.println("round " + i);
        }
        for (int row = 1; row <= 3; row++) {
            String line = "";
            for (int col = 1; col <= 3; col++) {
                line += row * col + " ";
            }
            System.out.println(line);
        }
    }
}`,['A for loop has three parts: start, condition and step.','The loop variable only exists inside the loop.','A loop inside a loop (nested) is how you walk through rows and columns.']),
      L('flow',6,'break and continue','Leave a loop early or skip one round.',`public class Main {
    public static void main(String[] args) {
        for (int i = 1; i <= 6; i++) {
            if (i == 2) continue;
            if (i == 5) break;
            System.out.println(i);
        }
    }
}`,['continue skips the rest of this round and moves to the next.','break leaves the loop completely.','Use them sparingly: a clear condition is usually easier to read.']),
      L('flow',7,'Arrays','Store many values of one type.',`public class Main {
    public static void main(String[] args) {
        int[] nums = {5, 3, 8};
        nums[1] = 9;
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }
        System.out.println(sum);
        for (int n : nums) System.out.println(n);
        String[] names = new String[2];
        names[0] = "Ana";
        System.out.println(names[0] + " " + names[1]);
    }
}`,['An array has a fixed length, and its first index is 0.','array.length is the size (no parentheses), so the last index is length - 1.','The for-each loop visits every element without an index.']),
      L('flow',8,'The Math class','Ready-made maths helpers.',`public class Main {
    public static void main(String[] args) {
        System.out.println(Math.max(4, 9));
        System.out.println(Math.min(4, 9));
        System.out.println(Math.abs(-7));
        System.out.println(Math.pow(2, 10));
        System.out.println(Math.sqrt(81));
        System.out.println(Math.round(2.6));
        System.out.println(Math.ceil(2.1) + " " + Math.floor(2.9));
    }
}`,['Math methods are static, so you call them as Math.name(...).','Math.pow and Math.sqrt return a double.','Math.round gives the nearest whole number; ceil rounds up and floor rounds down.']),
    ] },
);
Object.assign(W3_LESSON, {
  'flow-1': 'java_conditions.asp', 'flow-2': 'java_booleans.asp', 'flow-3': 'java_switch.asp', 'flow-4': 'java_while_loop.asp',
  'flow-5': 'java_for_loop.asp', 'flow-6': 'java_break.asp', 'flow-7': 'java_arrays.asp', 'flow-8': 'java_math.asp',
});
