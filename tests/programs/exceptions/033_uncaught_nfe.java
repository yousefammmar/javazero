public class Main {
    public static void main(String[] args) {
        String[] inputs = {"10", "20", "3x", "40"};
        int total = 0;
        for (String s : inputs) {
            total += Integer.parseInt(s);
            System.out.println("total " + total);
        }
    }
}
