import java.util.concurrent.atomic.*;
public class Main {
    static int plain = 0;
    static synchronized void inc() { plain++; }
    static final Object lock = new Object();
    static int guarded = 0;
    public static void main(String[] args) throws Exception {
        System.out.println(Thread.currentThread().getName());
        Thread t1 = new Thread(() -> System.out.println("hello from t1"));
        t1.start(); t1.join();
        Thread t2 = new Thread(new Runnable() { public void run() { System.out.println("anon runnable"); } });
        t2.start(); t2.join();
        class Worker extends Thread {
            int id; Worker(int id) { this.id = id; }
            @Override public void run() { for (int i = 0; i < 1000; i++) inc(); }
        }
        Worker w1 = new Worker(1), w2 = new Worker(2);
        w1.start(); w2.start(); w1.join(); w2.join();
        System.out.println("plain=" + plain);
        AtomicInteger ai = new AtomicInteger();
        AtomicLong al = new AtomicLong(10);
        Runnable r = () -> { for (int i = 0; i < 500; i++) { ai.incrementAndGet(); al.addAndGet(2); synchronized (lock) { guarded++; } } };
        Thread a = new Thread(r), b = new Thread(r), c = new Thread(r);
        a.start(); b.start(); c.start();
        a.join(); b.join(); c.join();
        System.out.println(ai.get() + " " + al.get() + " " + guarded);
        System.out.println(ai.compareAndSet(1500, 7) + " " + ai.get() + " " + ai.getAndIncrement() + " " + ai.updateAndGet(x -> x * 3) + " " + ai.accumulateAndGet(5, Math::max));
        Thread t = new Thread(() -> { throw new IllegalStateException("boom"); });
        t.setUncaughtExceptionHandler((th, ex) -> System.out.println("handler got " + ex.getMessage()));
        t.start(); t.join();
        System.out.println(t.isAlive() + " " + Thread.currentThread().isAlive());
        Thread s = new Thread(() -> { try { Thread.sleep(10); System.out.println("slept"); } catch (InterruptedException e) { } });
        s.start(); s.join();
        Thread d = new Thread(() -> {});
        System.out.println(d.getState());
        d.start(); d.join();
        System.out.println(d.getState());
    }
}
