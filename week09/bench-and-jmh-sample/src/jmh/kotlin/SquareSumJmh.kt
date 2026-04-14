package pt.isel

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.infra.Blackhole
import kotlin.math.sqrt

@BenchmarkMode(Mode.AverageTime) // Measure execution time per operation
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
open class SquareSumJmh {
    private val iterations = 1_000_000


    @Benchmark
    fun benchmarkSquareSumDeadCode() {
        var sum = 0.0
        for (i in 0 until iterations) {
            sum += sqrt(i.toDouble())
        }
        // Since the sum is not consumed, the JIT optimizes the code
        // by removing the preceding operations (dead code elimination).
    }

    @Benchmark
    fun benchmarkSquareSumWithBlackhole(bh: Blackhole) {
        var sum = 0.0
        for (i in 0 until iterations) {
            sum += sqrt(i.toDouble())
        }
        bh.consume(sum)
        //return sum // Also works
    }
}
