public class User {
    private String name;

    // This is the equivalent of the Companion Object members
    public static int userCount = 0;

    public static void incrementCount() {
        userCount++;
    }

    // Constructor
    public User(String name) {
        this.name = name;
        // Accessing the static method
        User.incrementCount();
    }

    public String getName() {
        return name;
    }
}
