import java.util.*;
import java.util.stream.*;
public class Main {
    enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }
    record Card(int rank, Suit suit) implements Comparable<Card> {
        public int compareTo(Card o) { return rank != o.rank ? Integer.compare(rank, o.rank) : suit.compareTo(o.suit); }
        public String toString() { return "" + "23456789TJQKA".charAt(rank - 2) + suit.name().charAt(0); }
    }
    public static void main(String[] args) {
        List<Card> deck = new ArrayList<>();
        for (Suit s : Suit.values()) for (int r = 2; r <= 14; r++) deck.add(new Card(r, s));
        System.out.println(deck.size() + " " + deck.subList(0, 5));
        Collections.shuffle(deck, new Random(42));
        System.out.println(deck.subList(0, 13));
        List<Card> hand = new ArrayList<>(deck.subList(0, 7));
        Collections.sort(hand);
        System.out.println(hand);
        Map<Suit, Long> hs = hand.stream().collect(Collectors.groupingBy(Card::suit, TreeMap::new, Collectors.counting()));
        System.out.println(hs);
        Map<Integer, List<Card>> byRank = new TreeMap<>();
        for (Card c : deck.subList(0, 20)) byRank.computeIfAbsent(c.rank(), k -> new ArrayList<>()).add(c);
        System.out.println(byRank);
        Map<Suit, Integer> bySuit = new TreeMap<>();
        for (Card c : deck.subList(0, 20)) bySuit.merge(c.suit(), c.rank(), Integer::sum);
        System.out.println(bySuit);
        TreeSet<Card> top = new TreeSet<>(Comparator.reverseOrder());
        top.addAll(deck.subList(0, 10));
        System.out.println(top.first() + " " + top.last() + " " + top.size());
        PriorityQueue<Card> pq = new PriorityQueue<>(deck.subList(0, 6));
        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) sb.append(pq.poll()).append(' ');
        System.out.println(sb.toString().trim());
        System.out.println(Collections.max(deck) + " " + Collections.min(deck) + " " + deck.indexOf(new Card(14, Suit.SPADES)) + " " + deck.contains(new Card(2, Suit.CLUBS)));
        System.out.println(new Card(5, Suit.HEARTS).equals(new Card(5, Suit.HEARTS)) + " " + new Card(5, Suit.HEARTS).hashCode() * 0);
        List<List<Card>> hands = new ArrayList<>();
        for (int p = 0; p < 4; p++) hands.add(new ArrayList<>());
        for (int i = 0; i < 12; i++) hands.get(i % 4).add(deck.get(i));
        for (List<Card> h : hands) { Collections.sort(h); System.out.println(h + " sum " + h.stream().mapToInt(Card::rank).sum()); }
        Set<Suit> seen = new TreeSet<>();
        for (Card c : deck) { if (seen.add(c.suit())) System.out.print(c.suit() + " "); if (seen.size() == 4) break; }
        System.out.println();
        System.out.println(Arrays.toString(Suit.values()) + " " + Suit.valueOf("HEARTS").ordinal() + " " + Suit.HEARTS.compareTo(Suit.CLUBS));
        Map<String, Suit> lookup = new TreeMap<>();
        for (Suit s : Suit.values()) lookup.put(s.name().toLowerCase(), s);
        System.out.println(lookup);
    }
}
