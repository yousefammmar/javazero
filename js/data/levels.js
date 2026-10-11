/* The three learning levels. Each level owns some module "tracks" (interactive lessons) and a set of W3Schools topic groups.
   Topic = [title, w3 path under /java/, optional interactive lesson id, optional description]. Descriptions are original text. */
const LEVELS = [
  { id: 'basics', name: 'Basics', page: 'basics.html', icon: 'book', tracks: ['fund', 'logic', 'oop', 'inh'], weeks: 'Weeks 1 to 10', audience: 'No experience needed',
    tagline: 'Start from zero',
    desc: 'Your first steps in Java: variables, data types, operators, decisions, loops, arrays and methods, then the building blocks of object-oriented code: classes, constructors, inheritance and interfaces.',
    groups: [
      { name: 'Java Tutorial', blurb: 'The language fundamentals, from your first program to loops and arrays.', topics: [
        ['Java Intro', 'java_intro.asp'], ['Java Get Started', 'java_getstarted.asp'], ['Java Syntax', 'java_syntax.asp'], ['Java Output', 'java_output.asp'], ['Java Comments', 'java_comments.asp'],
        ['Java Variables', 'java_variables.asp', 'vars-1'], ['Java Data Types', 'java_data_types.asp', 'vars-2'], ['Java Type Casting', 'java_type_casting.asp', 'vars-6'], ['Java Operators', 'java_operators.asp', 'vars-4'],
        ['Java Strings', 'java_strings.asp', 'vars-5'], ['Java Math', 'java_math.asp', 'flow-8'], ['Java Booleans', 'java_booleans.asp', 'flow-2'], ['Java If...Else', 'java_conditions.asp', 'flow-1'],
        ['Java Switch', 'java_switch.asp', 'flow-3'], ['Java While Loop', 'java_while_loop.asp', 'flow-4'], ['Java For Loop', 'java_for_loop.asp', 'flow-5'], ['Java Break/Continue', 'java_break.asp', 'flow-6'], ['Java Arrays', 'java_arrays.asp', 'flow-7'] ] },
      { name: 'Java Methods', blurb: 'Reusable blocks of code: parameters, return values, overloading, scope and recursion.', topics: [
        ['Java Methods', 'java_methods.asp', 'methods-1'], ['Method Parameters', 'java_methods_param.asp', 'methods-2'], ['Return Values', 'java_methods_return.asp', 'methods-3'],
        ['Method Overloading', 'java_methods_overloading.asp', 'methods-5'], ['Java Scope', 'java_scope.asp', 'methods-4'], ['Java Recursion', 'java_recursion.asp', 'methods-6'] ] },
      { name: 'Java Classes', blurb: 'Object-oriented programming: classes, objects, constructors, inheritance and interfaces.', topics: [
        ['Java OOP', 'java_oop.asp'], ['Classes/Objects', 'java_classes.asp', 'classes-1'], ['Class Attributes', 'java_class_attributes.asp', 'classes-2'], ['Class Methods', 'java_class_methods.asp'],
        ['Constructors', 'java_constructors.asp', 'classes-3'], ['The this Keyword', 'java_this.asp', 'classes-4'], ['Modifiers', 'java_modifiers.asp', 'classes-6'], ['Encapsulation', 'java_encapsulation.asp', 'classes-5'],
        ['Inheritance', 'java_inheritance.asp', 'inherit-1'], ['The super Keyword', 'java_super.asp', 'inherit-2'], ['Polymorphism', 'java_polymorphism.asp', 'interfaces-4'], ['Interfaces', 'java_interface.asp', 'interfaces-1'] ] },
    ] },
  { id: 'intermediate', name: 'Intermediate', page: 'intermediate.html', icon: 'network', tracks: ['mid'], weeks: 'Weeks 11 to 18', audience: 'Comfortable with the Basics',
    tagline: 'Write real programs',
    desc: 'Write programs that handle the real world: abstraction, enums and nested classes, exceptions and debugging, the collections framework (lists, sets and maps), plus generics and lambdas. Finish Basics first.',
    groups: [
      { name: 'Java Classes, going further', blurb: 'Abstraction, enums, nested classes, packages and reading user input.', topics: [
        ['Non-Access Modifiers', 'java_non_modifiers.asp'], ['Packages / API', 'java_packages.asp'], ['Inner Classes', 'java_inner_classes.asp', 'abstraction-6'], ['Abstraction', 'java_abstract.asp', 'abstraction-1'],
        ['Anonymous Classes', 'java_anonymous.asp'], ['Enums', 'java_enums.asp', 'abstraction-3'], ['Enum Constructor', 'java_enum_constructor.asp', 'abstraction-4'], ['User Input', 'java_user_input.asp'], ['Java Date', 'java_date.asp'] ] },
      { name: 'Java Errors', blurb: 'Read error messages, debug, and handle exceptions so programs keep running.', topics: [
        ['Java Errors', 'java_errors.asp'], ['Java Debugging', 'java_debugging.asp'], ['Exceptions (try/catch)', 'java_try_catch.asp', 'exceptions-1'], ['Multiple Exceptions', 'java_exceptions_multiple.asp', 'exceptions-3'], ['try-with-resources', 'java_try_catch_resources.asp'] ] },
      { name: 'Java Data Structures', blurb: 'The collections framework: lists, sets, maps, iterators and algorithms.', topics: [
        ['Data Structures', 'java_data_structures.asp'], ['Collections', 'java_collections.asp'], ['List', 'java_list.asp'], ['ArrayList', 'java_arraylist.asp', 'collections-1'], ['LinkedList', 'java_linkedlist.asp', 'collections-2'],
        ['List Sorting', 'java_sort_list.asp', 'collections-7'], ['Set', 'java_set.asp'], ['HashSet', 'java_hashset.asp', 'collections-4'], ['TreeSet', 'java_treeset.asp'], ['LinkedHashSet', 'java_linkedhashset.asp'],
        ['Map', 'java_map.asp'], ['HashMap', 'java_hashmap.asp', 'collections-3'], ['TreeMap', 'java_treemap.asp', 'collections-5'], ['LinkedHashMap', 'java_linkedhashmap.asp', 'collections-5'], ['Iterator', 'java_iterator.asp', 'collections-6'], ['Algorithms', 'java_algorithms.asp'] ] },
    ] },
  { id: 'advanced', name: 'Advanced', page: 'advanced.html', icon: 'zap', tracks: ['adv'], weeks: 'Weeks 19 to 22', audience: 'Comfortable with the Intermediate level',
    tagline: 'Go beyond the everyday toolkit',
    desc: 'The topics in W3Schools\' "Java Advanced" section: wrapper classes, generics, annotations, regular expressions, threads, lambdas and advanced sorting, plus file handling and I/O streams that you will try on your own computer.',
    groups: [
      { name: 'Java Advanced', blurb: 'The advanced topics from W3Schools. Generics and Lambda are taught in the Intermediate track.', topics: [
        ['Wrapper Classes', 'java_wrapper_classes.asp', 'wrappers-1', 'Objects that wrap primitive values, and how autoboxing moves between them.'],
        ['Generics', 'java_generics.asp', 'lambdas-1', 'Classes and methods that work with any type, checked by the compiler.'],
        ['Annotations', 'java_annotations.asp', 'annotations-1', 'Metadata markers such as @Override that compilers and frameworks can read.'],
        ['RegEx', 'java_regex.asp', 'regex-1', 'Patterns that describe text, for searching, validating and replacing.'],
        ['Threads', 'java_threads.asp', 'threads-1', 'Running several tasks at once and keeping shared data safe.'],
        ['Lambda', 'java_lambda.asp', 'lambdas-3', 'Short anonymous functions you can pass around like values.'],
        ['Advanced Sorting', 'java_advanced_sorting.asp', 'sorting-1', 'Comparable and Comparator, for sorting objects by your own rules.'] ] },
      { name: 'Java File Handling', run: false, blurb: 'Create, write, read and delete files.', topics: [
        ['Java Files', 'java_files.asp', '', 'The File class and working with paths.'], ['Create Files', 'java_files_create.asp', '', 'Make a new empty file.'], ['Write Files', 'java_files_write.asp', '', 'Save text into a file.'],
        ['Read Files', 'java_files_read.asp', '', 'Load a file back into your program.'], ['Delete Files', 'java_files_delete.asp', '', 'Remove files and folders.'] ] },
      { name: 'Java I/O Streams', run: false, blurb: 'Byte and character streams for reading and writing data.', topics: [
        ['Java I/O Streams', 'java_io_streams.asp', '', 'How data flows in and out of a program.'], ['FileInputStream', 'java_fileinputstream.asp', '', 'Read raw bytes from a file.'], ['FileOutputStream', 'java_fileoutputstream.asp', '', 'Write raw bytes to a file.'],
        ['BufferedReader', 'java_bufferedreader.asp', '', 'Read text efficiently, line by line.'], ['BufferedWriter', 'java_bufferedwriter.asp', '', 'Write text efficiently.'] ] },
      { name: 'Next steps', blurb: 'Put everything together.', topics: [ ['Java Projects', 'java_projects.asp', '', 'Practice by building complete programs.'] ] },
    ] },
];

const levelOfTrack = t => LEVELS.find(l => l.tracks.includes(t));
const levelOfModule = m => levelOfTrack(m.track);
const levelOfLesson = id => { const m = MODULES.find(x => x.id === String(id).split('-')[0]); return m && levelOfModule(m); };
const levelModules = lv => MODULES.filter(m => lv.tracks.includes(m.track));
const levelStats = lv => { const mods = levelModules(lv), d = Progress.done(), all = mods.flatMap(m => m.lessons), n = all.filter(l => d.has(l.id)).length;
  return { modules: mods.length, lessons: all.length, done: n, pct: all.length ? Math.round(n / all.length * 100) : 0, minutes: mods.reduce((a, m) => a + m.min, 0), topics: lv.groups.reduce((a, g) => a + g.topics.length, 0) }; };
