import java.util.*;
public class Main {
    public static void main(String[] args) {
        LinkedList<String> ll = new LinkedList<>();
        ll.add("b"); ll.addFirst("a"); ll.addLast("c"); ll.add(1, "x");
        System.out.println(ll);
        System.out.println(ll.getFirst() + ll.getLast() + ll.peek() + ll.peekFirst() + ll.peekLast());
        System.out.println(ll.pop() + " " + ll);
        ll.push("z");
        System.out.println(ll);
        System.out.println(ll.pollLast() + " " + ll.pollFirst() + " " + ll);
        ll.offer("q"); ll.offerFirst("p"); ll.offerLast("r");
        System.out.println(ll + " size " + ll.size());
        System.out.println(ll.removeFirst() + ll.removeLast() + " " + ll);
        System.out.println(ll.indexOf("q") + " " + ll.contains("x"));
        ll.remove("x");
        System.out.println(ll);
        ll.removeFirstOccurrence("q");
        System.out.println(ll + " " + ll.isEmpty());
        System.out.println(ll.poll() + " " + ll.poll() + " " + ll.peek());
        try { ll.getFirst(); } catch (NoSuchElementException e) { System.out.println("NSEE getFirst"); }
        try { ll.removeLast(); } catch (NoSuchElementException e) { System.out.println("NSEE removeLast"); }
        List<Integer> list = new LinkedList<>(List.of(5, 6, 7, 8));
        list.add(2, 99);
        list.remove(Integer.valueOf(7));
        list.remove(0);
        System.out.println(list + " get(1)=" + list.get(1));
        Iterator<Integer> di = ((LinkedList<Integer>) list).descendingIterator();
        while (di.hasNext()) System.out.print(di.next() + ",");
        System.out.println();
        ListIterator<Integer> li = list.listIterator();
        while (li.hasNext()) { int v = li.next(); if (v == 99) li.remove(); else li.set(v + 1); }
        System.out.println(list);
        Deque<Integer> dq = new LinkedList<>();
        for (int i = 0; i < 5; i++) { if (i % 2 == 0) dq.addFirst(i); else dq.addLast(i); }
        System.out.println(dq);
        System.out.println(new LinkedList<>(List.of(1, 2)).equals(new ArrayList<>(List.of(1, 2))));
    }
}
