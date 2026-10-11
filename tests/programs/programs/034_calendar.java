public class Main {
    static boolean leap(int y) { return (y % 4 == 0 && y % 100 != 0) || y % 400 == 0; }

    static int daysIn(int y, int m) {
        return switch (m) {
            case 2 -> leap(y) ? 29 : 28;
            case 4, 6, 9, 11 -> 30;
            default -> 31;
        };
    }

    // Zeller: 0=Saturday
    static int zeller(int y, int m, int d) {
        if (m < 3) { m += 12; y--; }
        int k = y % 100, j = y / 100;
        return (d + 13 * (m + 1) / 5 + k + k / 4 + j / 4 + 5 * j) % 7;
    }

    static final String[] DAYS = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"};

    static int dayOfYear(int y, int m, int d) {
        int n = d;
        for (int i = 1; i < m; i++) n += daysIn(y, i);
        return n;
    }

    static long daysSinceEpoch(int y, int m, int d) {
        long n = 0;
        for (int yy = 1970; yy < y; yy++) n += leap(yy) ? 366 : 365;
        return n + dayOfYear(y, m, d) - 1;
    }

    static void printMonth(int y, int m) {
        System.out.println("   " + MONTHS[m - 1] + " " + y);
        System.out.println("Su Mo Tu We Th Fr Sa");
        int first = (zeller(y, m, 1) + 6) % 7; // 0=Sunday
        StringBuilder sb = new StringBuilder("   ".repeat(first));
        for (int d = 1; d <= daysIn(y, m); d++) {
            sb.append(String.format("%2d", d));
            if ((first + d) % 7 == 0) { System.out.println(sb); sb.setLength(0); }
            else sb.append(' ');
        }
        if (sb.length() > 0) System.out.println(sb.toString().stripTrailing());
        System.out.println();
    }

    public static void main(String[] args) {
        for (int y : new int[]{1900, 2000, 2023, 2024, 2100, 2400}) System.out.println(y + " leap? " + leap(y));
        int[][] dates = {{2000, 1, 1}, {1969, 7, 20}, {2024, 2, 29}, {2038, 1, 19}, {1999, 12, 31}, {1776, 7, 4}};
        for (int[] dt : dates) {
            System.out.printf("%04d-%02d-%02d is a %s (day %d of year)%n", dt[0], dt[1], dt[2],
                DAYS[zeller(dt[0], dt[1], dt[2])], dayOfYear(dt[0], dt[1], dt[2]));
        }
        printMonth(2024, 2);
        printMonth(2023, 10);
        printMonth(2026, 11);
        System.out.println("Days from 1970-01-01 to 2000-01-01: " + daysSinceEpoch(2000, 1, 1));
        System.out.println("Days from 2000-01-01 to 2024-02-29: " + (daysSinceEpoch(2024, 2, 29) - daysSinceEpoch(2000, 1, 1)));
        int fridays13 = 0;
        for (int y = 2020; y <= 2030; y++)
            for (int m = 1; m <= 12; m++) if (zeller(y, m, 13) == 6) fridays13++;
        System.out.println("Friday the 13ths 2020-2030: " + fridays13);
    }
}
