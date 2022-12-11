package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.Input

fun main() {

  val rucksacks = Input("_2022/03-1.txt").nonEmptyLines

  fun String.splitHalf(): Pair<String, String> {
    val half = length/2
    return substring(0,half) to substring(half, length)
  }

  fun Pair<String,String>.duplicate(): Set<Char> {
    return first.toSet().intersect(second.toSet())
  }

  fun points(c:Char) : Int =   if (c.isUpperCase()) c.code - 38 else c.code - 96

  // 8252
  fun part1(): Int {
    return rucksacks.map {
      val p = it.splitHalf()
      val dup = p.duplicate().single()
      points(dup)
    }.sum()
  }

  fun part2(): Int {
   return  rucksacks.chunked(3)
      .map { it[0].toSet().intersect(it[1].toSet()).intersect(it[2].toSet()).single() }
      .map { points(it) }
      .sum()
  }

  println("jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL".splitHalf())
  println("jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL".splitHalf().duplicate())
  println("jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL".splitHalf().duplicate().first().code - 96)
  println(part2())

 // 65-90
  //97-122
}
