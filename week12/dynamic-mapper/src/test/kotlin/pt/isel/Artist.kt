package pt.isel

class Artist(
    val id: Int,
    val name: String,
    val kind: String,
    val country: Country,
    val tracks: List<Track>
)/*{
    override fun toString(): String {
        return "name: $name, country: $country, kind: $kind, tracks: ${tracks.joinToString(", ", prefix = "[", postfix = "]")}"
    }
}*/