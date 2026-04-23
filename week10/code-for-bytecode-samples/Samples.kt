import kotlin.math.sqrt

class Samples {

    fun sum(x: Float, y: Float) : Float {
        return x + y
    }

    fun modulus(x: Float, y: Float) : Float {
        val res = sqrt(x*x + y*y)
        return res
    }

    fun calculateNetBalance(
        balance: Int,
        tax: Float,
        interest: Float,
        income: Int,
        expense: Float
    ): Float {
        return balance - balance * tax + balance * interest + income - expense
    }

    fun mx(a: Float) : Float {
        return a * 1000
    }
}