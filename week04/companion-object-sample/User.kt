class User(val name: String) {
    
    companion object {
        var userCount = 0
	private set // remove method set
    }

    init {
        // Every time a new User is created, the count goes up
	userCount++
    }
}
