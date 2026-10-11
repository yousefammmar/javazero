import java.util.*;

public class Main {
    static int search(int[] a, int key) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] == key) return mid;
            if (a[mid] < key) lo = mid + 1; else hi = mid - 1;
        }
        return -1;
    }

    static int lowerBound(int[] a, int key) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < key) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    static int upperBound(int[] a, int key) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] <= key) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    static int searchRec(int[] a, int key, int lo, int hi) {
        if (lo > hi) return -(lo + 1);
        int mid = (lo + hi) / 2;
        if (a[mid] == key) return mid;
        return a[mid] < key ? searchRec(a, key, mid + 1, hi) : searchRec(a, key, lo, mid - 1);
    }

    static long isqrt(long n) {
        long lo = 0, hi = 3_037_000_499L;
        while (lo < hi) {
            long mid = (lo + hi + 1) / 2;
            if (mid * mid <= n) lo = mid; else hi = mid - 1;
        }
        return lo;
    }

    static int rotatedMin(int[] a) {
        int lo = 0, hi = a.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (a[mid] > a[hi]) lo = mid + 1; else hi = mid;
        }
        return a[lo];
    }

    public static void main(String[] args) {
        int[] a = {1, 3, 3, 3, 5, 8, 8, 13, 21, 34, 55};
        for (int k : new int[]{1, 3, 8, 55, 4, 0, 100}) {
            System.out.printf("key=%3d idx=%2d lower=%2d upper=%2d count=%d rec=%d lib=%d%n",
                k, search(a, k), lowerBound(a, k), upperBound(a, k), upperBound(a, k) - lowerBound(a, k),
                searchRec(a, k, 0, a.length - 1), Arrays.binarySearch(a, k) >= 0 ? 1 : 0);
        }
        long[] tests = {0, 1, 15, 16, 17, 99, 100, 1_000_000_007L, 4_611_686_014_132_420_609L};
        for (long t : tests) System.out.println("isqrt(" + t + ") = " + isqrt(t));
        System.out.println("rotated min: " + rotatedMin(new int[]{6, 7, 8, 9, 1, 2, 3, 4, 5}));
        System.out.println("rotated min: " + rotatedMin(new int[]{1, 2, 3}));
        // guess the number game
        int secret = 737, lo = 1, hi = 1000, guesses = 0;
        while (true) {
            int g = (lo + hi) / 2;
            guesses++;
            if (g == secret) { System.out.println("Found " + g + " in " + guesses + " guesses"); break; }
            else if (g < secret) lo = g + 1;
            else hi = g - 1;
        }
    }
}
