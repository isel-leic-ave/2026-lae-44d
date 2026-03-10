class Test {
    public static void main(String[] args) {
        String s = "ISEL";
        Object obj = s; // implicit upcast
        String s2 = (String) obj; // downcast, checked at runtime successfully
        // Integer n = (Integer) obj; // downcast, throws ClassCastException at runtime
    }
}