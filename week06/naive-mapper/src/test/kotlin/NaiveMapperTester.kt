package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NaiveMapperTest {
    @Test
    fun mapArtistSpotifyToArtistMutableVersion1() {
        val source = ArtistSpotify("Muse", "UK", "Band")
        // Properties in class Artist need to be declared as var.
        // For this reason, we use ArtistMutable instead.
        val dest: ArtistMutable = source.mapToProps(ArtistMutable::class)
        assertEquals(source.name, dest.name)
        assertEquals(source.kind, dest.kind)
        // 'country' and 'origin' are not yet associated in this implementation.
        assertEquals("", dest.origin)
    }
    @Test
    fun mapArtistSpotifyToArtistVersion2() {
        val source = ArtistSpotify("Muse", "UK", "Band")
        val dest:Artist = source.mapTo(Artist::class)
        assertEquals(source.name, dest.name)
        assertEquals(source.kind, dest.kind)
        // 'country' and 'from' are not yet associated in this implementation.
        assertEquals("", dest.from)
    }
    @Test
    fun mapArtistSpotifyToArtistVersionWithClass() {
        val mapper = NaiveMapper(ArtistSpotify::class, Artist::class)
        val source = ArtistSpotify("Muse", "UK", "Band")
        val dest:Artist = mapper.mapFrom(source)
        assertEquals(source.name, dest.name)
        assertEquals(source.kind, dest.kind)
        // 'country' and 'from' are not yet associated in this implementation.
        assertEquals("", dest.from)
    }
}