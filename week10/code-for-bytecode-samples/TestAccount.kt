class Account {
    var balance: Int = 100

    companion object {
        private var total = 0
        fun printTotal() { println(total) }
    }

    fun addBalance(value: Int) {
        balance += value  // getfield and putfield
    }

    private fun addTotal() {
        total += 1 // putstatic and getstatic
    }
}

fun testNewAccount() {
    // new, dup, invokespecial
	val account1 = Account()
}

fun testPrintTotal(){
    // It's not use invokestatic. Uses an invokevirtual.
    Account.printTotal()
}

fun testLambda() {
    // invokedynamic example:
    val f = { y: Int -> 5 + y }
    f(5)
}

fun main(){
    testNewAccount()
}