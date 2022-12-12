package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil.Input

fun main() {

  fun range(string: String): Set<Int> {
    val (a, b) = string.split("-").map { it.toInt() }
    return IntRange(a, b).toSet()
  }

  val input = Input("_2022/04-1.txt").nonEmptyLines
    .map {
      val (a, b) = it.split(",").map(::range)
      a to b
    }

  fun Pair<Set<Int>,Set<Int>>.contained(): Boolean {

    return first.containsAll(second) || second.containsAll(first)

  }

  fun part1(): Int = input.count { it.contained() }
  fun part2(): Int = input.count { it.first.intersect(it.second).isNotEmpty() }


  println(part1())
  println(part2())


}
