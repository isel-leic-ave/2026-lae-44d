package pt.isel

// Source class sample.
// The annotation indicates an alias for the property origin.
class ArtistSpotify(
    val name: String,
    @MapProp("country") val origin: String,
    val kind: String
)