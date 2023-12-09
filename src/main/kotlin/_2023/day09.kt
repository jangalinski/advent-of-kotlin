package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.StringExt.intValues

fun main() {

  fun diffs(list: List<Int>) = if (!list.all { it == 0 })
    list.windowed(2).map { (a, b) -> b - a }
  else
    emptyList()

  fun sequence(list: List<Int>) = generateSequence(list) {
    diffs(it)
  }.takeWhile(List<Int>::isNotEmpty).toList()


  val input = AoCUtil.Input(year = 2023, day = 9, test = false)

//  input.nonEmptyLines.map { it.intValues() }
//    .map {
//      generateSequence(it) {
//        diffs(it)
//      }.takeWhile(List<Int>::isNotEmpty).toList().map { it.last() }
//    }.map {
//      it.reversed().fold(0) {a,c -> a+c}
//    }
//    .sum()
//    .also { println(it) }

  // gold
  input.nonEmptyLines.map { it.intValues() }.map { sequence(it) }
    .map { it.map { it.first() }.reversed() }
    .map {
      it.fold(0) { a, c -> c - a }
    }
    .sum()
    .also { println(it) }
}
