class Test {

	public static void main(String[] args){
		Outer outerObject = new Outer("ISEL");
		Outer.Inner innerObject = outerObject.new Inner();
		innerObject.print();

		Outer.StaticNested.print(); // print is static
		//(new Outer.StaticNested()).print(); // print is not static
	}
}
