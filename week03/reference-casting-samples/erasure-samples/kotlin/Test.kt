fun main() {
	val names = mutableListOf<String>()
	val raw = names as MutableList<Any> // warning unchecked cast
	raw.add(123) // unchecked operation
	val first = names[0] // runtime cast check occurs here
}
