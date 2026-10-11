class Animal {
    String name;
    Animal(String name) { this.name = name; }
}
class Dog extends Animal {
    Dog() {
        System.out.println("dog");
    }
}
public class Main {
    public static void main(String[] args) {
        new Dog();
    }
}
