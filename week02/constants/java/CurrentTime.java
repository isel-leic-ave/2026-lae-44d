class CurrentTime {
    final long created1 = System.currentTimeMillis(); // Option 1 for final
    final long created2;
    final long created3;
    static final long createdStatic1 = System.currentTimeMillis(); // Option 1 for static final
    static final long createdStatic2;

    // Constructor
    CurrentTime(){
        created2 = System.currentTimeMillis(); // Option 2 for final
    }

    // Instance initializer block
    {
        created3 = System.currentTimeMillis(); // Option 3 for final
    }

    // Static initializer block
    static {
        createdStatic2 = System.currentTimeMillis(); // Option 2 for static final
    }

}
