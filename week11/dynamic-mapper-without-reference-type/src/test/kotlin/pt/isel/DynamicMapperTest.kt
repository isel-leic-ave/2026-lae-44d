package pt.isel

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DynamicMapperTest {
    private val source = ArtistSpotify(1,"Pearl Jam", "Band")
    @Test
    fun genBytecodeJavaBaseline() {
        val m = ArtistSpotify2ArtistBaseline()
        val dest = m.mapFrom(source)
        assertEquals(source.id, dest.id)
        assertEquals(source.name, dest.name)
        assertEquals(source.kind, dest.kind)
    }
    @Test
    fun mapArtistSpotifyToArtistNaiveMapperReflect() {
        val m = NaiveMapperReflect.mapper(ArtistSpotify::class, Artist::class)
        val dest = m.mapFrom(source)
        assertEquals(source.name, dest.name)
        assertEquals(source.kind, dest.kind)
    }
    @Test
    fun mapArtistSpotifyToArtist() {
        val dest:Artist =
            DynamicMapperSimple.loadDynamicMapper(ArtistSpotify::class, Artist::class)
                .mapFrom(source)
        assertEquals(source.id, dest.id)
        assertEquals(source.name, dest.name)
        assertEquals(source.kind, dest.kind)
    }
}