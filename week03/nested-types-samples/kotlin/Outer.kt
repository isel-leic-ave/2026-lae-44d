class Outer(private val name: String) {
	// Inner class (non-static)
	inner class Inner {
		fun print() {
			println("Outer name: ${name}")
		}
	}

	// Nested class (static)
	class Nested
}
