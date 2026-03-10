class FunctionTypeSamples {

	// * Lambda declaration:
	val isEven1: (Int) -> Boolean = { n -> n % 2 == 0 }
	// val isEven = { n: Int -> n % 2 == 0 } // The same as above
	val sum1: (Int, Int) -> Int = { a, b -> a + b }
	// val sum1 = { a: Int, b: Int -> a + b } // The same as above
	
	// * Anonymous declaration:
	val isEven2 = fun(n: Int): Boolean { return n % 2 == 0 }
	val sum2 = fun(a: Int, b: Int): Int { return a + b }

	// * Lambda with receiver:
	val isEven3: Int.() -> Boolean = { -> this % 2 == 0 }
	val sum3: Int.(Int) -> Int = { b -> this + b }

	// * Anonymous function with receiver:
	// val isEven4 = fun Int.(): Boolean { return this % 2 == 0 }
	// val sum4 = fun Int.(b: Int): Int { return this + b }
}
