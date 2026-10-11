public class Main {
    @SuppressWarnings("finally")
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            try {
                if (i == 1) continue;
                if (i == 3) break;
                System.out.println("body " + i);
            } finally {
                System.out.println("finally " + i);
            }
        }
        int n = 0;
        while (true) {
            try {
                n++;
                if (n < 3) continue;
                break;
            } finally {
                System.out.println("w finally " + n);
            }
        }
        outer:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                try {
                    if (j == 1) continue outer;
                    if (i == 2) break outer;
                    System.out.println("ij " + i + "," + j);
                } finally {
                    System.out.println("f " + i + "," + j);
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            try {
                throw new RuntimeException("r" + i);
            } catch (RuntimeException e) {
                System.out.println("caught " + e.getMessage());
                if (i == 1) continue;
            } finally {
                System.out.println("fin " + i);
            }
            System.out.println("after try " + i);
        }
        for (int i = 0; i < 3; i++) {
            try {
                throw new RuntimeException("swallowed" + i);
            } finally {
                if (i < 5) continue;
            }
        }
        System.out.println("swallow loop ended");
        int k = 0;
        do {
            try { k++; if (k == 2) break; }
            finally { System.out.println("do finally " + k); }
        } while (k < 10);
        switch (k) {
            case 2:
                try { System.out.println("in switch"); break; }
                finally { System.out.println("switch finally"); }
            default:
                System.out.println("not reached");
        }
    }
}
