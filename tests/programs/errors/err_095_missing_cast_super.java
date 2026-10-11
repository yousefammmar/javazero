class Animal { }
class Dog extends Animal {
    void bark() { }
}
public class Main {
    public static void main(String[] args) {
        Animal a = new Dog();
        Dog d = a;
        d.bark();
    }
}
