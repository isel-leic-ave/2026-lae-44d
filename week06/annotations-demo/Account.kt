@Color("Green")
class Account(
    @property:[Color("Blue") Tag] @param:Color("Yellow") var balance: Long,
    //@property:Color("Blue") @property:Tag @param:Color("Yellow")  var balance: Long, // Equivalent to the above line
    @property:Color("Red") val owner: String
) {
    @Tag fun deposit(@Color("Orange") value: Long) {
        balance += value
    }
}