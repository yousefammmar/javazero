import java.util.*;

public class Main {
    enum Direction { UP, DOWN, IDLE }

    static class Elevator {
        int floor = 0;
        Direction dir = Direction.IDLE;
        final TreeSet<Integer> stops = new TreeSet<>();
        int moved = 0;

        void request(int f) {
            if (f != floor) stops.add(f);
        }

        void step() {
            if (stops.isEmpty()) { dir = Direction.IDLE; return; }
            if (dir == Direction.IDLE) dir = stops.first() > floor ? Direction.UP : Direction.DOWN;
            if (dir == Direction.UP && stops.ceiling(floor) == null) dir = Direction.DOWN;
            if (dir == Direction.DOWN && stops.floor(floor) == null) dir = Direction.UP;
            floor += dir == Direction.UP ? 1 : -1;
            moved++;
            if (stops.remove(floor)) System.out.println("  doors open at floor " + floor);
        }
    }

    public static void main(String[] args) {
        Elevator e = new Elevator();
        Random rnd = new Random(7);
        int tick = 0;
        e.request(5);
        e.request(2);
        e.request(8);
        while (tick < 40) {
            tick++;
            if (tick == 6) { e.request(1); e.request(9); }
            if (tick == 15) e.request(4);
            if (tick % 11 == 0) e.request(rnd.nextInt(10));
            Direction before = e.dir;
            e.step();
            System.out.printf("t=%02d floor=%d dir=%-4s (was %-4s) pending=%s%n", tick, e.floor, e.dir, before, e.stops);
            if (e.stops.isEmpty() && tick > 16) break;
        }
        System.out.println("Total floors travelled: " + e.moved);
        System.out.println("Final state: floor " + e.floor + ", " + e.dir);
    }
}
