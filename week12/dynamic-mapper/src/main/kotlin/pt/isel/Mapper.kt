package pt.isel

interface Mapper<T, R> {
    fun mapFrom(src: T): R

    fun mapFromList(src: List<T>): List<R> {
        // call the mapProm function for each element of the source list.
        return src.stream().map { src: T -> this.mapFrom(src) }.toList()
    }
}