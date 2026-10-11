public class Main {
    static class AppException extends Exception {
        private final int code;
        public AppException(String message, int code) { super(message); this.code = code; }
        public AppException(String message, int code, Throwable cause) { super(message, cause); this.code = code; }
        public AppException(Throwable cause) { super(cause); this.code = -1; }
        public AppException() { this.code = 0; }
        public int getCode() { return code; }
        @Override public String toString() { return "AppException[code=" + code + ", msg=" + getMessage() + "]"; }
    }
    static class ValidationException extends AppException {
        private final String field;
        ValidationException(String field, String msg) { super(field + ": " + msg, 400); this.field = field; }
        String getField() { return field; }
    }
    static class Plain extends Exception { }
    static void validate(String name, int age) throws ValidationException {
        if (name == null || name.isEmpty()) throw new ValidationException("name", "must not be empty");
        if (age < 0) throw new ValidationException("age", "must be >= 0 but was " + age);
    }
    static void process(String n, int a) throws AppException {
        try { validate(n, a); System.out.println("valid " + n + " " + a); }
        catch (ValidationException e) { throw new AppException("processing failed", 500, e); }
    }
    public static void main(String[] args) {
        String[] names = {"bob", "", "amy"};
        int[] ages = {3, 4, -9};
        for (int i = 0; i < 3; i++) {
            try { process(names[i], ages[i]); }
            catch (AppException e) {
                System.out.println(e + " code=" + e.getCode());
                Throwable c = e.getCause();
                if (c instanceof ValidationException v) System.out.println("  field=" + v.getField() + " msg=" + v.getMessage() + " code=" + v.getCode());
            }
        }
        AppException a = new AppException(new RuntimeException("boom"));
        System.out.println(a.getMessage() + " | " + a.getCode());
        System.out.println(new AppException().getMessage() + " | " + new AppException());
        System.out.println(new Plain() + " | " + new Plain().getMessage());
        Exception e = new ValidationException("f", "m");
        System.out.println(e instanceof AppException);
        System.out.println(e.getClass().getName() + " " + e.getClass().getSuperclass().getSimpleName());
    }
}
