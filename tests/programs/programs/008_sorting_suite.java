import java.util.*;

public class Main {
    static long comparisons;

    static boolean less(int a, int b) { comparisons++; return a < b; }

    static void bubble(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < a.length - 1 - i; j++)
                if (less(a[j + 1], a[j])) { int t = a[j]; a[j] = a[j + 1]; a[j + 1] = t; swapped = true; }
            if (!swapped) break;
        }
    }

    static void insertion(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i], j = i - 1;
            while (j >= 0 && less(key, a[j])) { a[j + 1] = a[j]; j--; }
            a[j + 1] = key;
        }
    }

    static void selection(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int m = i;
            for (int j = i + 1; j < a.length; j++) if (less(a[j], a[m])) m = j;
            int t = a[i]; a[i] = a[m]; a[m] = t;
        }
    }

    static void mergeSort(int[] a, int lo, int hi) {
        if (hi - lo < 2) return;
        int mid = (lo + hi) / 2;
        mergeSort(a, lo, mid); mergeSort(a, mid, hi);
        int[] tmp = new int[hi - lo];
        int i = lo, j = mid, k = 0;
        while (i < mid && j < hi) tmp[k++] = less(a[j], a[i]) ? a[j++] : a[i++];
        while (i < mid) tmp[k++] = a[i++];
        while (j < hi) tmp[k++] = a[j++];
        System.arraycopy(tmp, 0, a, lo, tmp.length);
    }

    static void quick(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int p = a[hi], i = lo;
        for (int j = lo; j < hi; j++)
            if (less(a[j], p)) { int t = a[i]; a[i] = a[j]; a[j] = t; i++; }
        int t = a[i]; a[i] = a[hi]; a[hi] = t;
        quick(a, lo, i - 1); quick(a, i + 1, hi);
    }

    static void sift(int[] a, int n, int i) {
        while (true) {
            int l = 2 * i + 1, r = l + 1, big = i;
            if (l < n && less(a[big], a[l])) big = l;
            if (r < n && less(a[big], a[r])) big = r;
            if (big == i) return;
            int t = a[i]; a[i] = a[big]; a[big] = t;
            i = big;
        }
    }

    static void heap(int[] a) {
        for (int i = a.length / 2 - 1; i >= 0; i--) sift(a, a.length, i);
        for (int end = a.length - 1; end > 0; end--) {
            int t = a[0]; a[0] = a[end]; a[end] = t;
            sift(a, end, 0);
        }
    }

    interface Sorter { void sort(int[] a); }

    public static void main(String[] args) {
        Random rnd = new Random(7);
        int[] data = new int[30];
        for (int i = 0; i < data.length; i++) data[i] = rnd.nextInt(100);
        System.out.println("Input: " + Arrays.toString(data));
        int[] expected = data.clone();
        Arrays.sort(expected);
        System.out.println("Sorted: " + Arrays.toString(expected));
        String[] names = {"bubble", "insertion", "selection", "merge", "quick", "heap"};
        Sorter[] sorters = {
            Main::bubble, Main::insertion, Main::selection,
            a -> mergeSort(a, 0, a.length), a -> quick(a, 0, a.length - 1), Main::heap
        };
        for (int s = 0; s < sorters.length; s++) {
            int[] copy = data.clone();
            comparisons = 0;
            sorters[s].sort(copy);
            System.out.printf("%-10s ok=%b comparisons=%d%n", names[s], Arrays.equals(copy, expected), comparisons);
        }
        int[] sortedIn = expected.clone();
        comparisons = 0;
        bubble(sortedIn);
        System.out.println("bubble on sorted input: " + comparisons);
    }
}
