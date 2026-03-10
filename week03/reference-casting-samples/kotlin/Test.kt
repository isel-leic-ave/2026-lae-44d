fun main() {
	val s: String = "ISEL"
	foo(s) // implicit upcast
}

fun foo(obj: Any) : Int{
	val s: String= obj as String // explicit downcast, checked at runtime
	return s.length
}
