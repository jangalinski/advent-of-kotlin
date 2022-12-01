package io.github.jangalinski.aoc22

fun main() {
  val input = Input("01-1.txt")

  val readLongArrays = input.content.split("\n\n").map {block ->
    block.lines()
      .mapNotNull { if(it.trim().isEmpty()) null else it.trim().toLong() }
  }

  fun silver(): Long = readLongArrays.map { it.sum() }.maxOf { it }

  fun gold(): Long = readLongArrays.map { it.sum() }.sortedDescending().take(3).sum()

  println("01-silver: ${silver()}")
  println("01-gold:   ${gold()}")
}
