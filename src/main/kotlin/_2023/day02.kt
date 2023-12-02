package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.ListExt.peekPrint
import io.github.jangalinski.aoc.AoCUtil.StringExt.splitTrimmed
import io.github.jangalinski.aoc.AoCUtil.StringExt.toPair

typealias IntTriple = Triple<Int, Int, Int>

// TRIPLE: RGB - red, green blue
fun main() {
  fun IntTriple.max(other: IntTriple) = IntTriple(
    kotlin.math.max(this.first, other.first),
    kotlin.math.max(this.second, other.second),
    kotlin.math.max(this.third, other.third)
  )

  operator fun IntTriple.plus(other: IntTriple) = IntTriple(this.first + other.first, this.second + other.second, this.third + other.third)
  operator fun IntTriple.plus(pair: Pair<String, Int>) = when (pair.first) {
    "red" -> copy(first = first + pair.second)
    "green" -> copy(second = second + pair.second)
    else -> copy(third = third + pair.second)
  }
  fun IntTriple.power() = first * second * third

  fun List<IntTriple>.sum() = fold(IntTriple(0, 0, 0), IntTriple::plus)

  fun List<Triple<Int, Int, Int>>.max() = fold(IntTriple(0, 0, 0), IntTriple::max)

  fun parse(line: String): Pair<Int, List<IntTriple>> {
    val (g, d) = line.toPair(":")
    val data = d.splitTrimmed(";").map {
      it.splitTrimmed(",")
        .map {
          val (n, c) = it.splitTrimmed(" ")
          c to n.toInt()
        }
        .fold(IntTriple(0, 0, 0), IntTriple::plus)
    }
    return g.toPair(" ").second.toInt() to data
  }

  fun silver(input: AoCUtil.Input): Int = input.nonEmptyLines.map { parse(it) }
    .map { it.first to it.second.max() }
    .filter { IntTriple(12, 13, 14) == IntTriple(12, 13, 14).max(it.second) }
    .sumOf { it.first }

  fun gold(input: AoCUtil.Input) : Int = input.nonEmptyLines.map(::parse)
    .map { it.second.max().power() }
    .sum()


//  println(silver(AoCUtil.Input(2023, 2, test = true)))
  println(gold(AoCUtil.Input(2023, 2, test = false)))
}
