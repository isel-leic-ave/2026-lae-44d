# Lab Guide: Sequences

## Table of Contents

1. [Objectives](#objectives)
2. [Part 1: Eager vs. Laxy](#part-1-eager-vs-lazy)
3. [Part 2: Implementing Sequences](#part-2-implementing-sequences)

## Objectives

- Distinguish between Eager and Lazy evaluation: Understand the fundamental differences in how Iterable (Collections) and Sequence process data.

- Analyze Pipeline Execution: Trace the execution flow of functional transformations (map, filter, etc.) to predict performance and output.

- Implement Custom Sequences: Build custom sequence operators using sequence generator or by explicitly implementing the Iterator interface and managing internal state machines.

--- 

## Part 1: Eager vs. Lazy

1. Consider the following Kotlin code:
   ```kotlin
   arrayOf("abc", "isel", "super")
      .map { print("$it "); it.length }
      .filter { print("$it "); it == 4 }
      .first()
   ```
   
   a) What is the output of executing the given code?

   b) If you used sequenceOf instead of `arrayOf`, would there be any difference? Justify your answer.

2. Consider the following Kotlin code:
   ```kotlin
   val res = arrayOf("abcdef", "super", "isel", "trio", "tri")
      .map { print("$it "); it.length }
      .filter { print("$it "); it % 2 == 0 }
      .distinct()
   ```
   a) What is printed in the output when this code is executed, and what is stored in the res variable?

   b) What is printed in the output when this code is executed using sequenceOf instead of `arrayOf`? Justify your answer.

## Part 2: Implementing Sequences

1. Implement `fun <T> Sequence<T>.concat(other: Sequence<T>): Sequence<T>`. This function should return the concatenation of two sequences starting by the `this` sequence.
2. Implement `lazyFilter` using Sequence equivalent to `filter`.
    - Consider two different approaches:
        1. Using the explicit implementation of the interface `Iterator`.
        2. Using the generator `sequence`.
    - Consider use the following auxiliary states: `enum class FilterIteratorState { NOT_READY, READY, FINISHED }`
3. Implement `fun <T : Any?> Sequence<T>.collapse() = Sequence<T>`. This function should return a sequence without repeating subsequent elements.
   - _e.g._, `sequenceOf(1, 1, 4, 4, 4, 5, 1, 2, 2)` should return `[1, 4, 5, 1, 2]`.
   - _e.g._, `sequenceOf(null, null, null, "a", "a", "b", "a")` should return `[null, "a", "b", "a"]`.