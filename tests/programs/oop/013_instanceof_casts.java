import java.util.*;

public class Main {
    static class Vehicle { public String toString() { return "Vehicle"; } }
    static class Car extends Vehicle { int doors = 4; public String toString() { return "Car" + doors; } }
    static class SportsCar extends Car { { doors = 2; } public String toString() { return "Sports" + doors; } }
    static class Bike extends Vehicle { public String toString() { return "Bike"; } }
    static String describe(Object o) {
        if (o instanceof SportsCar sc && sc.doors == 2) return "sports2:" + sc;
        if (o instanceof Car c) return "car:" + c.doors;
        if (o instanceof Vehicle v) return "vehicle:" + v;
        if (!(o instanceof String s)) return "not-string:" + o;
        return "string:" + s.length();
    }
    public static void main(String[] args) {
        Object[] things = { new Vehicle(), new Car(), new SportsCar(), new Bike(), "hello", 42, null, new int[0] == null ? 1 : 2 };
        for (Object o : things) System.out.println(describe(o));
        Vehicle v = new SportsCar();
        System.out.println((v instanceof Car) + " " + (v instanceof SportsCar) + " " + (v instanceof Bike == false));
        System.out.println((null instanceof Object));
        Car c = (Car) v;
        System.out.println(c);
        try {
            Bike b = (Bike) (Object) v;
            System.out.println("no " + b);
        } catch (ClassCastException e) { System.out.println("CCE bike"); }
        try {
            Vehicle plain = new Vehicle();
            Car cc = (Car) plain;
            System.out.println("no " + cc);
        } catch (ClassCastException e) { System.out.println("CCE car"); }
        try {
            Object o = "str";
            Integer i = (Integer) o;
            System.out.println("no " + i);
        } catch (ClassCastException e) { System.out.println("CCE integer"); }
        Object nul = null;
        String sn = (String) nul;
        System.out.println(sn);
        Object num = 3;
        if (num instanceof Integer i && i > 2) System.out.println("big int " + (i + 1));
        if (num instanceof Number n) System.out.println("number " + n.doubleValue());
        System.out.println(num instanceof Comparable<?>);
        List<Object> mixed = new ArrayList<>(List.of(1, "a", 2.5, 'c', 10L));
        int ints = 0, strs = 0, others = 0;
        for (Object o : mixed) { if (o instanceof Integer) ints++; else if (o instanceof String) strs++; else others++; }
        System.out.println(ints + " " + strs + " " + others);
        double d = 3.99; int trunc = (int) d; long big = (long) 1e18; byte nb = (byte) 200; char ch = (char) 66;
        System.out.println(trunc + " " + big + " " + nb + " " + ch + " " + (int) 'A' + " " + (short) 70000);
    }
}
