class Outer {

	private final String name;
	// constructor
	Outer(String name) { this.name = name; }

	// Inner class (not static)
	class Inner {
		void print() { System.out.println("Outer name: " + name); }
	}

	// Static nested class
	static class StaticNested {
		static void print() {
			System.out.println("Printing from a static nested class... ");
			//System.out.println("Printing name: " + name); // Error: name is not accessible because is not static.
		}
	}
}
