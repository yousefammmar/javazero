import java.util.*;

public class Main {
    static class BST<T extends Comparable<T>> {
        private class Node {
            T key; Node left, right;
            Node(T k) { key = k; }
        }
        private Node root; private int size;

        void insert(T k) { root = insert(root, k); }
        private Node insert(Node n, T k) {
            if (n == null) { size++; return new Node(k); }
            int c = k.compareTo(n.key);
            if (c < 0) n.left = insert(n.left, k);
            else if (c > 0) n.right = insert(n.right, k);
            return n;
        }
        boolean contains(T k) {
            Node n = root;
            while (n != null) {
                int c = k.compareTo(n.key);
                if (c == 0) return true;
                n = c < 0 ? n.left : n.right;
            }
            return false;
        }
        int height() { return height(root); }
        private int height(Node n) { return n == null ? 0 : 1 + Math.max(height(n.left), height(n.right)); }
        void inorder(List<T> out) { inorder(root, out); }
        private void inorder(Node n, List<T> out) {
            if (n == null) return;
            inorder(n.left, out); out.add(n.key); inorder(n.right, out);
        }
        void preorder(List<T> out) { pre(root, out); }
        private void pre(Node n, List<T> out) {
            if (n == null) return;
            out.add(n.key); pre(n.left, out); pre(n.right, out);
        }
        List<List<T>> levels() {
            List<List<T>> res = new ArrayList<>();
            if (root == null) return res;
            Deque<Node> q = new ArrayDeque<>();
            q.add(root);
            while (!q.isEmpty()) {
                int n = q.size();
                List<T> lvl = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    Node x = q.poll();
                    lvl.add(x.key);
                    if (x.left != null) q.add(x.left);
                    if (x.right != null) q.add(x.right);
                }
                res.add(lvl);
            }
            return res;
        }
        int size() { return size; }
    }

    static class MinHeap {
        private int[] a = new int[4]; private int n;
        void add(int v) {
            if (n == a.length) a = Arrays.copyOf(a, n * 2);
            a[n] = v;
            int i = n++;
            while (i > 0 && a[(i - 1) / 2] > a[i]) {
                int p = (i - 1) / 2, t = a[p]; a[p] = a[i]; a[i] = t; i = p;
            }
        }
        int poll() {
            int top = a[0];
            a[0] = a[--n];
            int i = 0;
            while (true) {
                int l = 2 * i + 1, r = l + 1, s = i;
                if (l < n && a[l] < a[s]) s = l;
                if (r < n && a[r] < a[s]) s = r;
                if (s == i) break;
                int t = a[s]; a[s] = a[i]; a[i] = t; i = s;
            }
            return top;
        }
        boolean isEmpty() { return n == 0; }
    }

    public static void main(String[] args) {
        BST<Integer> t = new BST<>();
        for (int v : new int[]{50, 30, 70, 20, 40, 60, 80, 35, 45, 65, 30}) t.insert(v);
        List<Integer> out = new ArrayList<>();
        t.inorder(out);
        System.out.println("inorder  " + out);
        out.clear(); t.preorder(out);
        System.out.println("preorder " + out);
        System.out.println("levels   " + t.levels());
        System.out.println("size=" + t.size() + " height=" + t.height());
        System.out.println(t.contains(45) + " " + t.contains(46));
        BST<String> words = new BST<>();
        for (String w : "the quick brown fox jumps over the lazy dog".split(" ")) words.insert(w);
        List<String> ws = new ArrayList<>();
        words.inorder(ws);
        System.out.println(ws + " size=" + words.size());

        MinHeap h = new MinHeap();
        Random r = new Random(7);
        for (int i = 0; i < 12; i++) h.add(r.nextInt(50));
        StringBuilder sb = new StringBuilder();
        while (!h.isEmpty()) sb.append(h.poll()).append(' ');
        System.out.println(sb.toString().trim());
    }
}
