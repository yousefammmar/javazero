/* Compiler errors and exceptions, translated into plain English. */
const COMPILER_ERRORS = [
  { e: "error: ';' expected at line 14", say: "You forgot to terminate your statement with a semicolon ; on line 14 or immediately on line 13. In Java, semicolons are mandatory punctuation.", fix: "Quick Fix: Look right before line 14 and verify every statement terminates with ';'" },
  { e: 'error: cannot find symbol', say: "Java doesn't know what a name refers to. You likely misspelled a variable or method, or forgot to declare or import it.", fix: 'Quick Fix: Check spelling and capitalization (Java is case-sensitive) and add any missing import.' },
  { e: 'error: incompatible types: String cannot be converted to int', say: "You're putting text into a box built for whole numbers. Java won't convert types silently.", fix: 'Quick Fix: Use Integer.parseInt(text) or change the variable type to String.' },
  { e: 'error: missing return statement', say: 'Your method promises to hand back a value, but some path through it ends without a return.', fix: 'Quick Fix: Make sure every if/else branch ends with a return statement.' },
  { e: 'error: class Foo is public, should be declared in a file named Foo.java', say: 'A public class must live in a file with exactly the same name.', fix: 'Quick Fix: Rename the file to match the class, or remove public.' },
  { e: 'error: variable x might not have been initialized', say: 'You read a local variable before giving it a value.', fix: 'Quick Fix: Assign a value when you declare it: int x = 0;' },
];
const EXCEPTIONS = [
  { n: 'NullPointerException', frames: ['com.javazero.User.getName(User.java:24)', 'com.javazero.App.main(App.java:12)'], say: 'You used a reference that points to nothing (null) as if it were an object.', fix: 'Initialize User user = new User() before calling methods.' },
  { n: 'ArrayIndexOutOfBoundsException', frames: ['com.javazero.Grades.average(Grades.java:9)', 'com.javazero.App.main(App.java:6)'], say: 'You asked for an array slot that does not exist. Valid indexes run from 0 to length - 1.', fix: 'Change i <= arr.length to i < arr.length.' },
  { n: 'NumberFormatException', frames: ['com.javazero.Parser.read(Parser.java:5)', 'com.javazero.App.main(App.java:3)'], say: 'Integer.parseInt received text that is not a whole number.', fix: 'Validate the input or catch NumberFormatException.' },
  { n: 'ArithmeticException', frames: ['com.javazero.Calc.divide(Calc.java:4)', 'com.javazero.App.main(App.java:8)'], say: 'You divided an integer by zero.', fix: 'Check that the divisor is not 0 before dividing.' },
  { n: 'InputMismatchException', frames: ['java.util.Scanner.throwFor(Scanner.java:939)', 'java.util.Scanner.nextInt(Scanner.java:2117)', 'com.javazero.App.main(App.java:7)'], say: 'The program asked for a whole number but the input was something else, such as text.', fix: 'Check the value you typed, or read it with nextLine() and validate it before parsing.' },
  { n: 'NoSuchElementException', frames: ['java.util.Scanner.nextLine(Scanner.java:1651)', 'com.javazero.App.main(App.java:6)'], say: 'The program tried to read input, but there was none left.', fix: 'Provide enough input, or guard the read with hasNextLine() or hasNextInt().' },
  { n: 'StackOverflowError', frames: ['com.javazero.Math.fact(Math.java:3)', 'com.javazero.Math.fact(Math.java:3)', '... 1022 more'], say: 'A recursive method never reached its base case.', fix: 'Add a base case such as if (n <= 1) return 1;' },
];
