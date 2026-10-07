/* External reading: links to W3Schools Java reference pages (content stays on w3schools.com). */
const W3 = 'https://www.w3schools.com/java/';
const W3_LESSON = {
  'vars-1': 'java_variables.asp', 'vars-2': 'java_data_types.asp', 'vars-3': 'java_variables.asp', 'vars-4': 'java_operators.asp',
  'vars-5': 'java_strings.asp', 'vars-6': 'java_type_casting.asp', 'vars-7': 'java_variables.asp', 'vars-8': 'java_syntax.asp',
  'methods-1': 'java_methods.asp', 'methods-2': 'java_methods_param.asp', 'methods-3': 'java_methods.asp', 'methods-4': 'java_scope.asp',
  'methods-5': 'java_methods_overloading.asp', 'methods-6': 'java_recursion.asp',
  'classes-1': 'java_classes.asp', 'classes-2': 'java_class_attributes.asp', 'classes-3': 'java_constructors.asp', 'classes-4': 'java_this.asp',
  'classes-5': 'java_encapsulation.asp', 'classes-6': 'java_modifiers.asp', 'classes-7': 'java_classes.asp',
  'inherit-1': 'java_inheritance.asp', 'inherit-2': 'java_inheritance.asp', 'inherit-3': 'java_polymorphism.asp', 'inherit-4': 'java_modifiers.asp', 'inherit-5': 'java_classes.asp',
  'interfaces-1': 'java_interface.asp', 'interfaces-2': 'java_interface.asp', 'interfaces-3': 'java_interface.asp', 'interfaces-4': 'java_polymorphism.asp',
};
const RESOURCES = [
  { t: 'Start here', icon: 'play', items: [['Java Tutorial home', '', 'The full beginner course, in order.'], ['Java Intro', 'java_intro.asp', 'What Java is and how it runs.'], ['Java Syntax', 'java_syntax.asp', 'Your first program, line by line.']] },
  { t: 'Fundamentals', icon: 'cpu', items: [['Variables', 'java_variables.asp', 'Declaring, assigning, final.'], ['Data Types', 'java_data_types.asp', 'The eight primitives and String.'], ['Type Casting', 'java_type_casting.asp', 'Widening and narrowing.'], ['Operators', 'java_operators.asp', 'Math, comparison and logic.'], ['Strings', 'java_strings.asp', 'Methods and immutability.'], ['If ... Else', 'java_conditions.asp', 'Making decisions.'], ['For Loop', 'java_for_loop.asp', 'Repeating work.'], ['Arrays', 'java_arrays.asp', 'Fixed-size lists.']] },
  { t: 'Methods & scope', icon: 'sigma', items: [['Methods', 'java_methods.asp', 'Anatomy and calling.'], ['Parameters', 'java_methods_param.asp', 'Passing values in.'], ['Overloading', 'java_methods_overloading.asp', 'Same name, different inputs.'], ['Scope', 'java_scope.asp', 'Where a variable lives.'], ['Recursion', 'java_recursion.asp', 'A method calling itself.']] },
  { t: 'Object-oriented Java', icon: 'wrench', items: [['Classes & Objects', 'java_classes.asp', 'Blueprints and instances.'], ['Class Attributes', 'java_class_attributes.asp', 'Fields on an object.'], ['Constructors', 'java_constructors.asp', 'Setting up new objects.'], ['The this keyword', 'java_this.asp', 'Referring to the current object.'], ['Modifiers', 'java_modifiers.asp', 'public, private, static, final.'], ['Encapsulation', 'java_encapsulation.asp', 'Hide data, expose behavior.'], ['Inheritance', 'java_inheritance.asp', 'extends and super.'], ['Polymorphism', 'java_polymorphism.asp', 'One interface, many forms.'], ['Interfaces', 'java_interface.asp', 'Contracts classes must follow.'], ['Abstraction', 'java_abstract.asp', 'Abstract classes and methods.']] },
  { t: 'Practice & reference', icon: 'target', items: [['Java Examples', 'java_examples.asp', 'Small programs to read and run.'], ['Java Exercises', 'java_exercises.asp', 'Fill-in-the-blank practice.'], ['Java Quiz', 'java_quiz.asp', 'Test yourself.'], ['Java Keywords', 'java_ref_keywords.asp', 'Quick reference for every keyword.'], ['Try Java online', 'java_compiler.asp', 'Run code in their editor.']] },
];
