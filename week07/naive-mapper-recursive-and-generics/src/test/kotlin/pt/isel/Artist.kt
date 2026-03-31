package pt.isel

class Artist(
    val name: String,
    val country: Country, // = Country("", ""),
    val kind: String,
    val tracks: List<Track>
){
    override fun toString(): String {
        return "name: $name, country: $country, kind: $kind, tracks: ${tracks.joinToString(", ", prefix = "[", postfix = "]")}"
    }
}