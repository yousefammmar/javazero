public class Main {
    static int counter = 0;
    static int next() { return ++counter; }
    static int[] data = new int[5];
    static int pos = 0;
    public static void main(String[] args) {
        int i = 5;
        int a = i++ + ++i;
        System.out.println(a + " " + i);
        int b = i-- - --i;
        System.out.println(b + " " + i);
        int c = i++ * 2 + i++ * 3;
        System.out.println(c + " " + i);
        i = 0;
        i = i++;
        System.out.println(i);
        i = i++ + i++;
        System.out.println(i);
        i = 10;
        i = ++i + i++;
        System.out.println(i);
        int x = 1;
        x += x++ * 10;
        System.out.println(x);
        int y = 2;
        y = y-- - y--;
        System.out.println(y);
        int j = 1;
        int r = j++ + j++ + j++;
        System.out.println(r + " " + j);
        r = ++j + ++j + ++j;
        System.out.println(r + " " + j);
        System.out.println(next() + next() * next());
        System.out.println(counter);
        System.out.println(next() - next() - next());
        data[pos++] = pos;
        data[pos++] = pos * 10;
        data[pos] = pos++;
        data[++pos] = 77;
        System.out.println(java.util.Arrays.toString(data) + " pos=" + pos);
        int[] arr = {0, 0, 0, 0};
        int k = 0;
        arr[k] = k = 2;
        System.out.println(java.util.Arrays.toString(arr) + " k=" + k);
        arr[k++] = k++;
        System.out.println(java.util.Arrays.toString(arr) + " k=" + k);
        byte bt = 127;
        bt++;
        System.out.println(bt);
        bt--;
        System.out.println(bt);
        byte b2 = bt++;
        System.out.println(b2 + " " + bt);
        char ch = 'a';
        char ch2 = ch++;
        char ch3 = ++ch;
        System.out.println(ch + " " + ch2 + " " + ch3);
        double d = 1.5;
        d++;
        System.out.println(d);
        d--; d--;
        System.out.println(d);
        float f = 0.5f;
        System.out.println(f++ + " " + ++f);
        long l = Long.MAX_VALUE;
        l++;
        System.out.println(l);
        Integer boxed = 10;
        boxed++;
        Integer other = boxed--;
        System.out.println(boxed + " " + other);
        int n = 3;
        int m = -n--;
        System.out.println(m + " " + n);
        int p = - -n;
        int q = -(-n);
        int s = +-n;
        System.out.println(p + " " + q + " " + s);
        int t = n+++n;
        System.out.println(t + " " + n);
        int u = n---n;
        System.out.println(u + " " + n);
        boolean flag = false;
        int calls = 0;
        if (flag && calls++ > 0) { }
        if (flag & calls++ > 0) { }
        if (!flag || calls++ > 0) { }
        if (!flag | calls++ > 0) { }
        System.out.println(calls);
        for (int w = 0, z = 10; w < z; w += 3, z -= 2) System.out.print(w + ":" + z + " ");
        System.out.println();
    }
}
