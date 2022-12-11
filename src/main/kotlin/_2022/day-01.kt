package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.Input

fun main() {
  val input = Input("_2022/01-1.txt")

  val readLongArrays = input.contentTrimmed.split("\n\n").map { block ->
    block.lines()
      .mapNotNull { if(it.trim().isEmpty()) null else it.trim().toLong() }
  }

  fun silver(): Long = readLongArrays.map { it.sum() }.maxOf { it }

  fun gold(): Long = readLongArrays.map { it.sum() }.sortedDescending().take(3).sum()

  println("01-silver: ${silver()}")
  println("01-gold:   ${gold()}")
}
