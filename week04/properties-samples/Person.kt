class Person(name: String) {
    var name: String = name
    set(value) {
        require(value.length <= 70)
        field = value
    }
}
