abstract class A() {
    abstract fun foo()
    open fun bar(){ println("bar") }
}

interface I {
    fun foo()
    fun bar(){ println("bar") }
}

class CA : A() {
    override fun foo() {}
    override fun bar() {}
}

class CI : I {
    override fun foo() {}
}
