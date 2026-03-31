package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class NaiveMapperTest {
    @Test
    fun mapArtistSpotifyToArtistVersion4() {
        val mapper = NaiveMapper(ArtistSpotify::class, Artist::class)
        //val mapper = NaiveMapperEnhanced.mapper(ArtistSpotify::class, Artist::class)
        val source = ArtistSpotify("Pearl Jam", "Band", State("USA", "English"),  listOf(
                Song("Jeremy", 1991),
                Song("Black", 1991),
                Song("Daughter", 1993)
            )
        )
        val dest:Artist = mapper.mapFrom(source)
        //println(dest)
        assertEquals(source.name, dest.name)
        assertEquals(source.kind, dest.kind)
        assertEquals(source.origin.name, dest.country.name)
        assertEquals(source.origin.idiom, dest.country.idiom)
        val tracks = dest.tracks.iterator()
        source.songs.forEach {
            val actual = tracks.next()
            assertEquals(it.name, actual.name)
            assertEquals(it.year, actual.year)
        }
        assertFalse { tracks.hasNext() }
    }
}