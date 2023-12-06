package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.StringExt.longValues
import io.github.jangalinski.aoc.AoCUtil.StringExt.toPair

fun main() {


  val input = AoCUtil.Input(year = 2023, day = 6, test = false)

  fun parse(input: AoCUtil.Input): List<Pair<Long, Long>> {
    val (a, b) = input.nonEmptyLines.map {
      it.toPair(":").second.longValues()
    }
    return a.zip(b)
  }

  fun results(n: Long): List<Long> = sequence<Long> {
    (0..n).forEach { b ->
      yield(b * (n - b))
    }
  }.toList()

  fun parseGold(input: AoCUtil.Input) : Pair<Long,Long> {
    val (a,b) = input.nonEmptyLines.map { it.toPair(":").second.longValues() }
      .map { it.joinToString(separator = "").toLong() }

    return a to b
  }


  val races = parse(input)

  println(parseGold(input))


//  val silver = races.map { r -> results(r.first).map { r to it }.filter { it.second > it.first.second }.size }
//    .fold(1) { a, c -> a * c }
//  println(silver)

  val gold = listOf(parseGold(input)).map { r -> results(r.first).map { r to it }.filter { it.second > it.first.second }.size }
    .fold(1) { a, c -> a * c }

println(gold)

}
