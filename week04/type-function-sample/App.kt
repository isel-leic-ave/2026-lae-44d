fun main() {

	val fts = FunctionTypeSamples()
	// Testing the functions of the class FunctionTypeSamples:
	println(fts.isEven1(10));
	println(fts.isEven2(10));
	println(fts.sum1(2, 4));
	println(fts.sum2(2, 4));


	// Testing the functions with FunctionTypeSamples context:
	with(fts){
		println(10.isEven3());
		println(2.sum3(4));
	}
}
