public class Main {
    static class Animal {
        String name = "animal";
        static String kind() { return "Animal.kind"; }
        String speak() { return "..."; }
        String who() { return "I am " + name + " says " + speak(); }
        String describe() { return kind(); }
    }
    static class Dog extends Animal {
        String name = "dog";
        static String kind() { return "Dog.kind"; }
        @Override String speak() { return "Woof"; }
        String both() { return name + "/" + super.name + "/" + ((Animal) this).name; }
        String superSpeak() { return super.speak(); }
    }
    static class Puppy extends Dog {
        String name = "puppy";
        @Override String speak() { return "Yip+" + super.speak(); }
    }
    public static void main(String[] args) {
        Animal a = new Dog();
        Dog d = new Dog();
        Animal p = new Puppy();
        System.out.println(a.name + " " + d.name + " " + p.name);
        System.out.println(((Dog) a).name + " " + ((Puppy) p).name);
        System.out.println(a.speak() + " " + d.speak() + " " + p.speak());
        System.out.println(a.who());
        System.out.println(p.who());
        System.out.println(a.kind() + " " + d.kind() + " " + p.kind());
        System.out.println(Animal.kind() + " " + Dog.kind());
        System.out.println(a.describe() + " " + d.describe());
        System.out.println(d.both());
        System.out.println(d.superSpeak());
        System.out.println(((Puppy) p).both());
        System.out.println(((Puppy) p).superSpeak());
        Animal[] zoo = { new Animal(), new Dog(), new Puppy() };
        for (Animal z : zoo) System.out.println(z.getClass().getSimpleName() + ": " + z.speak() + " / " + z.name);
    }
}
