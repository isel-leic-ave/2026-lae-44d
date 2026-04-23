interface PaymentMethod {
    fun process(value: Float)
}

class CreditCard : PaymentMethod {
    override fun process(value: Float) {
        println("Processing ${value} via Credit...")
    }
}

fun makePayment(p: PaymentMethod, c: CreditCard) {
    c.process(50.0F)
    p.process(100.0F)
}