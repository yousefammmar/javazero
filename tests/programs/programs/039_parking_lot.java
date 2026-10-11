import java.util.*;

public class Main {
    enum Size { SMALL, MEDIUM, LARGE }

    static abstract class Vehicle {
        final String plate;
        Vehicle(String plate) { this.plate = plate; }
        abstract Size size();
        abstract int ratePerHour();
        public String toString() { return getClass().getSimpleName() + "(" + plate + ")"; }
    }
    static class Motorcycle extends Vehicle {
        Motorcycle(String p) { super(p); }
        Size size() { return Size.SMALL; }
        int ratePerHour() { return 1; }
    }
    static class Car extends Vehicle {
        Car(String p) { super(p); }
        Size size() { return Size.MEDIUM; }
        int ratePerHour() { return 3; }
    }
    static class Truck extends Vehicle {
        Truck(String p) { super(p); }
        Size size() { return Size.LARGE; }
        int ratePerHour() { return 7; }
    }

    static class Lot {
        private final Map<Size, Integer> free = new EnumMap<>(Size.class);
        private final Map<String, Integer> entryTime = new HashMap<>();
        private final Map<String, Vehicle> parked = new TreeMap<>();
        private int revenue;

        Lot(int s, int m, int l) {
            free.put(Size.SMALL, s); free.put(Size.MEDIUM, m); free.put(Size.LARGE, l);
        }

        boolean enter(Vehicle v, int hour) {
            // vehicles may use a larger slot if their own size is full
            for (Size sz : Size.values()) {
                if (sz.compareTo(v.size()) < 0) continue;
                if (free.get(sz) > 0) {
                    free.merge(sz, -1, Integer::sum);
                    parked.put(v.plate, v);
                    entryTime.put(v.plate, hour);
                    System.out.println(hour + "h: " + v + " parks in " + sz + " slot");
                    return true;
                }
            }
            System.out.println(hour + "h: " + v + " turned away, lot full");
            return false;
        }

        void leave(String plate, int hour) {
            Vehicle v = parked.remove(plate);
            if (v == null) { System.out.println(hour + "h: " + plate + " not found"); return; }
            int hours = Math.max(1, hour - entryTime.remove(plate));
            int fee = hours * v.ratePerHour();
            revenue += fee;
            free.merge(v.size(), 1, Integer::sum);
            System.out.println(hour + "h: " + v + " leaves after " + hours + "h, pays " + fee);
        }
    }

    public static void main(String[] args) {
        Lot lot = new Lot(1, 2, 1);
        lot.enter(new Car("CAR-1"), 8);
        lot.enter(new Motorcycle("MOTO-1"), 8);
        lot.enter(new Motorcycle("MOTO-2"), 9);
        lot.enter(new Car("CAR-2"), 9);
        lot.enter(new Car("CAR-3"), 10);
        lot.enter(new Truck("TRK-1"), 10);
        lot.enter(new Truck("TRK-2"), 11);
        lot.leave("CAR-1", 12);
        lot.enter(new Car("CAR-4"), 12);
        lot.leave("MOTO-2", 12);
        lot.leave("NOPE", 13);
        lot.leave("TRK-1", 17);
        lot.leave("MOTO-1", 8);
        System.out.println("Parked now: " + lot.parked.keySet());
        System.out.println("Free: " + lot.free);
        System.out.println("Revenue: " + lot.revenue);
    }
}
