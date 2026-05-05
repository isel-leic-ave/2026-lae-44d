package pt.isel

class ArtistSpotify(
    val id: Int,
    val name: String,
    val kind: String,
    @MapProp("country") val origin: State,
    @MapProp("tracks") val songs: List<Song>
)