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
    private val source = ArtistSpotify("Pearl Jam", "Band", State("USA", "English"), listOf(
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

    val mapper = NaiveMapper(ArtistSpotify::class, Artist::class)
    val mapperEnhanced = NaiveMapperEnhanced.mapper(ArtistSpotify::class, Artist::class)

    @Benchmark
    fun mapArtistSpotifyToArtistMapTo(blackhole: Blackhole) {
        val dest = source.mapTo(Artist::class)
        blackhole.consume(dest)
    }
    @Benchmark
    fun mapArtistSpotifyToArtistNaiveMapper(blackhole: Blackhole) {
        val dest: Artist = mapper.mapFrom(source)
        blackhole.consume(dest)
    }
    @Benchmark
    fun mapArtistSpotifyToArtistNaiveMapperEnhanced(blackhole: Blackhole) {
        val dest: Artist = mapperEnhanced.mapFrom(source)
        blackhole.consume(dest)
    }
}