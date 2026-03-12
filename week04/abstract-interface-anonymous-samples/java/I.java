public interface I {
    int CONST = 100; // <=> public static final int CONST
    void foo(); // <=> abstract void foo()
    default void bar(){
        System.out.println("bar");
    };
}

