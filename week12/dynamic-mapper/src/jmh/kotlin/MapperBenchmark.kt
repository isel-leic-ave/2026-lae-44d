package pt.isel

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime) // Measure execution time per operation
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
open class NaiveMapperBenchmark {
    private val source = ArtistSpotify(1,"Pearl Jam", "Band", State("USA", "English"), listOf(
        Song("Jeremy", 1991),
        Song("Black", 1991),
        Song("Alive", 1991),
        Song("Even Flow", 1991),
        Song("Daughter", 1993),
        Song("Better Man", 1994),
        Song("Corduroy", 1994),
        Song("Hail, Hail", 1996),
        Song("Given to Fly", 1998),
        Song("Last Kiss", 1999),
        Song("Just Breathe", 2009)
    ))

    val mapper = NaiveMapperReflect.mapper(ArtistSpotify::class, Artist::class)
    val mapperDynamic = DynamicMapper.loadDynamicMapper(ArtistSpotify::class, Artist::class)
    val mapperBaseline = ArtistSpotify2ArtistBaseline()

    @Benchmark
    fun mapArtistSpotifyToArtistUsingBaselineMapperJava(blackHole: Blackhole) {
        val dest: Artist = mapperBaseline.mapFrom(source)
        blackHole.consume(dest)
    }
    @Benchmark
    fun mapArtistSpotifyToArtistManually(blackHole: Blackhole) {
        val dest = Artist(
            source.id,
            source.name,
            source.kind,
            Country(source.origin.name, source.origin.idiom),
            source.songs.map{ Track(it.name, it.year) }
        )
        blackHole.consume(dest)
    }
    @Benchmark
    fun mapArtistSpotifyToArtistUsingNaiveMapperReflect(blackHole: Blackhole) {
        val dest: Artist = mapper.mapFrom(source)
        blackHole.consume(dest)
    }
    @Benchmark
    fun mapArtistSpotifyToArtistUsingDynamicMapper(blackHole: Blackhole) {
        val dest: Artist = mapperDynamic.mapFrom(source)
        blackHole.consume(dest)
    }
}