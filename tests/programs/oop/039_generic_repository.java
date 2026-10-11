import java.util.*;
import java.util.function.*;

public class Main {
    interface Entity<ID extends Comparable<ID>> { ID id(); }
    interface Repo<T extends Entity<ID>, ID extends Comparable<ID>> {
        void save(T t);
        Optional<T> find(ID id);
        List<T> all();
        default int count() { return all().size(); }
        default List<T> where(Predicate<? super T> p) { List<T> r = new ArrayList<>(); for (T t : all()) if (p.test(t)) r.add(t); return r; }
    }
    static class MemRepo<T extends Entity<ID>, ID extends Comparable<ID>> implements Repo<T, ID> {
        private final Map<ID, T> store = new TreeMap<>();
        public void save(T t) { store.put(t.id(), t); }
        public Optional<T> find(ID id) { return Optional.ofNullable(store.get(id)); }
        public List<T> all() { return new ArrayList<>(store.values()); }
    }
    record User(Integer id, String name, int age) implements Entity<Integer> {}
    record Tag(String id, int uses) implements Entity<String> {}
    static <T extends Entity<ID>, ID extends Comparable<ID>> String ids(Repo<T, ID> r) {
        StringBuilder sb = new StringBuilder();
        for (T t : r.all()) sb.append(t.id()).append(' ');
        return sb.toString().trim();
    }
    public static void main(String[] args) {
        Repo<User, Integer> users = new MemRepo<>();
        users.save(new User(3, "Cy", 41)); users.save(new User(1, "Al", 25)); users.save(new User(2, "Bo", 33));
        users.save(new User(2, "Bo2", 34));
        System.out.println(users.count() + " " + ids(users));
        System.out.println(users.find(2).map(User::name).orElse("?") + " " + users.find(9).map(User::name).orElse("?"));
        System.out.println(users.where(u -> u.age() > 30));
        Repo<Tag, String> tags = new MemRepo<>();
        tags.save(new Tag("java", 5)); tags.save(new Tag("c", 2)); tags.save(new Tag("go", 7));
        System.out.println(ids(tags) + " " + tags.where(t -> t.uses() > 4).size());
        Optional<Tag> best = Optional.empty();
        for (Tag t : tags.all()) if (best.isEmpty() || t.uses() > best.get().uses()) best = Optional.of(t);
        System.out.println(best.get());
        System.out.println(users.find(1).isPresent() + " " + users.find(1).get().equals(new User(1, "Al", 25)));
        Optional<String> o = users.find(3).filter(u -> u.age() < 40).map(User::name);
        System.out.println(o + " " + o.isPresent() + " " + Optional.of("x") + " " + Optional.empty());
        users.find(1).ifPresent(u -> System.out.println("found " + u.name()));
        users.find(8).ifPresentOrElse(u -> System.out.println("found"), () -> System.out.println("none"));
    }
}
