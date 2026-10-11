public class Main {
    static class ConfigError extends RuntimeException {
        ConfigError(String m, Throwable c) { super(m, c); }
    }
    static void load(String key) {
        try { Integer.parseInt(key); }
        catch (NumberFormatException e) { throw new ConfigError("bad config key " + key, e); }
    }
    public static void main(String[] args) {
        load("12");
        System.out.println("12 loaded");
        try { load("zz"); } catch (ConfigError e) { System.out.println(e.getMessage() + " <- " + e.getCause().getMessage()); }
        load("q1");
        System.out.println("unreachable");
    }
}
