import java.util.*;
import java.util.concurrent.*;
public class Main {
    public static void main(String[] args) throws Exception {
        ExecutorService ex = Executors.newFixedThreadPool(3);
        List<Future<Integer>> fs = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            final int n = i;
            fs.add(ex.submit(() -> { int s = 0; for (int k = 1; k <= n * 100; k++) s += k; return s; }));
        }
        for (Future<Integer> f : fs) System.out.println(f.get());
        Future<String> fstr = ex.submit(() -> "done");
        System.out.println(fstr.get() + " " + fstr.isDone());
        Future<?> fr = ex.submit(() -> {});
        System.out.println(fr.get());
        Future<Integer> bad = ex.submit(() -> { if (true) throw new IllegalArgumentException("bad task"); return 1; });
        try { bad.get(); } catch (ExecutionException e) { System.out.println(e.getCause().getClass().getName() + ": " + e.getCause().getMessage()); }
        List<Callable<String>> cs = List.of(() -> "x", () -> "y", () -> "z");
        for (Future<String> f : ex.invokeAll(cs)) System.out.print(f.get());
        System.out.println();
        ex.shutdown();
        System.out.println(ex.awaitTermination(5, TimeUnit.SECONDS) + " " + ex.isShutdown());
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> 20).thenApply(x -> x + 1).thenCombine(CompletableFuture.completedFuture(2), (a, b) -> a * b);
        System.out.println(cf.get());
        ConcurrentHashMap<String, Integer> chm = new ConcurrentHashMap<>();
        ExecutorService ex2 = Executors.newFixedThreadPool(4);
        List<Future<?>> fl = new ArrayList<>();
        for (int i = 0; i < 4; i++) fl.add(ex2.submit(() -> { for (int k = 0; k < 250; k++) chm.merge("k", 1, Integer::sum); }));
        for (Future<?> f : fl) f.get();
        ex2.shutdown();
        System.out.println(chm);
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(latch::countDown).start();
        new Thread(latch::countDown).start();
        latch.await();
        System.out.println("latch " + latch.getCount());
    }
}
