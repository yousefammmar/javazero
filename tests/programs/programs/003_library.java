import java.util.*;

public class Main {
    enum Genre { FICTION, SCIENCE, HISTORY, FANTASY;
        int loanDays() { return switch (this) { case FICTION -> 14; case SCIENCE -> 21; case HISTORY -> 21; case FANTASY -> 10; }; }
    }

    interface Borrowable {
        boolean isAvailable();
        void borrow(String who) throws IllegalStateException;
        void giveBack();
    }

    static class Book implements Borrowable, Comparable<Book> {
        final String title; final String author; final Genre genre;
        private String borrower;
        private int timesBorrowed;
        Book(String t, String a, Genre g) { title = t; author = a; genre = g; }
        public boolean isAvailable() { return borrower == null; }
        public void borrow(String who) {
            if (!isAvailable()) throw new IllegalStateException(title + " already borrowed by " + borrower);
            borrower = who; timesBorrowed++;
        }
        public void giveBack() { borrower = null; }
        public int compareTo(Book o) { return title.compareTo(o.title); }
        public String toString() {
            return String.format("%-22s %-10s %-8s %s", title, author, genre, isAvailable() ? "in" : "out(" + borrower + ")");
        }
    }

    static class Library {
        private final List<Book> books = new ArrayList<>();
        void add(Book b) { books.add(b); }
        Book find(String title) {
            for (Book b : books) if (b.title.equalsIgnoreCase(title)) return b;
            return null;
        }
        void checkout(String title, String who) {
            Book b = find(title);
            if (b == null) { System.out.println("No such book: " + title); return; }
            try {
                b.borrow(who);
                System.out.println(who + " borrowed " + b.title + " for " + b.genre.loanDays() + " days");
            } catch (IllegalStateException e) {
                System.out.println("Cannot: " + e.getMessage());
            }
        }
        void report() {
            Collections.sort(books);
            for (Book b : books) System.out.println("  " + b);
        }
    }

    public static void main(String[] args) {
        Library lib = new Library();
        lib.add(new Book("Dune", "Herbert", Genre.FICTION));
        lib.add(new Book("Cosmos", "Sagan", Genre.SCIENCE));
        lib.add(new Book("The Hobbit", "Tolkien", Genre.FANTASY));
        lib.add(new Book("SPQR", "Beard", Genre.HISTORY));
        lib.add(new Book("A Brief History", "Hawking", Genre.SCIENCE));
        System.out.println("Initial:");
        lib.report();
        lib.checkout("dune", "ann");
        lib.checkout("Dune", "bob");
        lib.checkout("cosmos", "bob");
        lib.checkout("Missing", "cy");
        lib.checkout("The Hobbit", "ann");
        System.out.println("After checkouts:");
        lib.report();
        lib.find("Dune").giveBack();
        lib.checkout("Dune", "bob");
        System.out.println("Final:");
        lib.report();
        EnumMap<Genre, Integer> counts = new EnumMap<>(Genre.class);
        for (Book b : lib.books) counts.merge(b.genre, 1, Integer::sum);
        System.out.println(counts);
        for (Genre g : Genre.values()) System.out.println(g + " ordinal=" + g.ordinal() + " days=" + g.loanDays());
        System.out.println(Genre.valueOf("HISTORY").compareTo(Genre.FICTION));
    }
}
