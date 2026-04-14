package pt.isel

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole

@BenchmarkMode(Mode.AverageTime) // Measure execution time per operation
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
open class NextPrimeJmh {
    private val n = 7000000L

    @Benchmark
    //@OutputTimeUnit(TimeUnit.MILLISECONDS)
    fun benchNextPrimeWithBH(blackhole: Blackhole) {
        blackhole.consume(nextPrime(n))
    }

    @Benchmark
    //@OutputTimeUnit(TimeUnit.NANOSECONDS)
    fun benchNextPrime() {
        // It could be a dead code, but JIT can't remove code. So, it works.
        // In any case, this is unreliable and should be avoided.
        nextPrime(n)
    }


}
