package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class DynamicMapperTest {
    private val source = ArtistSpotify(1,"Pearl Jam", "Band",
        State("USA", "English"),
        listOf(
            Song("Jeremy", 1991),
            Song("Alive", 1991),
            Song("Daughter", 1993)
        )
    )
    @Test
    fun mapArtistSpotifyToArtistwithBaseline() {
        val mapper = ArtistSpotify2ArtistBaseline()
        val dest = mapper.mapFrom(source)
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
    @Test
    fun mapArtistSpotifyToArtistNaiveMapperReflect() {
        val mapper = NaiveMapperReflect.mapper(ArtistSpotify::class, Artist::class)
        val dest = mapper.mapFrom(source)
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
    @Test
    fun mapArtistSpotifyToArtistDynamicMapper() {
        val mapper = DynamicMapper.loadDynamicMapper(ArtistSpotify::class, Artist::class)
        val dest : Artist = mapper.mapFrom(source)
        assertEquals(source.id, dest.id)
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