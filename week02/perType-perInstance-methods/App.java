public class App {
    String name;

    // Main method
    public static void main(String[] args) {

        perTypeMethod(); // <=> App.perTypeMethod();

        new App("app1").perInstanceMethod();

        new App("app2").otherInstanceMethod();
    }

    // Constructor
    App(String name) {this.name = name; }

    // Per-instance method
    public void perInstanceMethod() {
        System.out.println("I am a per-instance method of " + name + " object");
    }

    // Per-type method
    public static void perTypeMethod() {
        System.out.println("I am a per-type method of class " + App.class.getSimpleName());
    }

    // Other per-instance method that calls the perInstanceMethod() of this object
    public void otherInstanceMethod() {
        perInstanceMethod(); // <=> this.perInstanceMethod()
    }
}
