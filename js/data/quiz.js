const QUIZ = [
  { q: 'How many bits is a Java int?', o: ['16', '32', '64', 'It depends on the machine'], a: 1, why: 'int is always 32 bits in Java, on every platform.' },
  { q: 'What does 7 / 2 evaluate to when both are ints?', o: ['3.5', '4', '3', '3.0'], a: 2, why: 'Integer division truncates the fraction.' },
  { q: 'Which is the right way to compare String contents?', o: ['a == b', 'a.equals(b)', 'a = b', 'a.same(b)'], a: 1, why: '== compares references; equals compares text.' },
  { q: 'What does void mean in a method signature?', o: ['It is private', 'It returns nothing', 'It is static', 'It takes no input'], a: 1, why: 'void means the method returns no value.' },
  { q: 'What does the new keyword do?', o: ['Declares a variable', 'Imports a class', 'Allocates an object on the heap', 'Starts the JVM'], a: 2, why: 'new allocates memory for an object and runs its constructor.' },
  { q: 'Which keyword makes a class inherit from another?', o: ['implements', 'extends', 'inherits', 'super'], a: 1, why: 'extends declares the parent class; implements is for interfaces.' },
  { q: 'What is the last valid index of an array with length 5?', o: ['5', '4', '6', '0'], a: 1, why: 'Arrays are 0-indexed, so valid indexes are 0 to 4.' },
  { q: 'Java source compiles to what?', o: ['Machine code', 'Bytecode (.class)', 'JavaScript', 'Assembly'], a: 1, why: 'javac produces bytecode that any JVM can run.' },
  { q: 'Which access modifier is the most restrictive?', o: ['public', 'protected', 'private', 'default'], a: 2, why: 'private is visible only inside its own class.' },
  { q: 'A class can implement how many interfaces?', o: ['Exactly one', 'At most two', 'Any number', 'None'], a: 2, why: 'A class can implement as many interfaces as it needs.' },
];
