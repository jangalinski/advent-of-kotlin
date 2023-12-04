package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.ListExt.peekPrint
import io.github.jangalinski.aoc._2023.D03.DIGITS
import io.github.jangalinski.aoc._2023.D03.borderSymbols
import io.github.jangalinski.aoc._2023.D03.extractNumbers
import io.github.jangalinski.aoc.cellValues
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.Row
import io.toolisticon.lib.krid.model.cells
import kotlin.math.max
import kotlin.math.min

object D03 {
  val DIGITS = Regex("\\d+")

  fun String.extractNumbers() = DIGITS.findAll(this).map { it.value.toInt() to it.range }.toList()

  fun borderSymbols(p: String, c: String, a: String, range: IntRange): Set<Char> {
    val r = (range.first - 1)..(range.last + 1)
    val cur = c.substring(r).replace(DIGITS, "")
    return (p.subSequence(r).toSet() + a.subSequence(r).toSet() + cur.toSet()).filterNot { '.' == it }.toSet()
  }
}

fun main() {
  val input = AoCUtil.Input(year = 2023, day = 3, test = true)

  val onlyGears = input.contentTrimmed.replace("""[.\-%+=/$&#@]""".toRegex(), ".").lines()
    .map { ".$it." }
    .windowed(3).map { (p, c, a) ->
      val nums = c.extractNumbers()
      val filtered = nums.map { Triple(it.first, it.second,borderSymbols(p, c, a, it.second)) }

      println(filtered)

      filtered.filter {
        it.third.isEmpty()
      }.fold(c) {acc,cur -> acc.replaceRange(cur.second, cur.first.toString().replace(DIGITS,"."))}
      c
    }
    .joinToString("\n")
  println(onlyGears)

//  [.,
//  , 4, 7, 9, 1, 5, 6, 2, 3, 8, -, %, +, =, *, /, $, &, 0, #, @]
  println(onlyGears.count { '*' == it })


  fun hasSymbolInBorder(p: String, c: String, a: String, num: Int, range: IntRange): Boolean {
    val r = (range.first - 1)..(range.last + 1)
    val cur = c.substring(r).replace(Regex("\\d+"), "")
    return (p.subSequence(r).toSet() + a.subSequence(r).toSet() + cur.toSet()).filterNot { '.' == it }.isNotEmpty()
  }

//  println(input.nonEmptyLines.map { ".$it." }
//    .windowed(3).map { (prev, act, after) ->
//
//      Regex("\\d+").findAll(act).map { it.range to it.value.toInt() }
//        .map {
//          val r = (it.first.first - 1)..(it.first.last + 1)
//          it.second to hasSymbolInBorder(prev,act,after,it.second,it.first)
//        }
//        .toList()
//        .filter { it.second }
//        .sumOf { it.first }
//    }.sum())

}

fun main1() {
  val input = AoCUtil.Input(year = 2023, day = 3, test = false)

  fun hasSymbolInBorder(p: String, c: String, a: String, num: Int, range: IntRange): Boolean {
    val r = (range.first - 1)..(range.last + 1)
    val cur = c.substring(r).replace(Regex("\\d+"), "")
    return (p.subSequence(r).toSet() + a.subSequence(r).toSet() + cur.toSet()).filterNot { '.' == it }.isNotEmpty()
  }

  println(input.nonEmptyLines.map { ".$it." }
    .windowed(3).map { (prev, act, after) ->

      Regex("\\d+").findAll(act).map { it.range to it.value.toInt() }
        .map {
          val r = (it.first.first - 1)..(it.first.last + 1)
          it.second to hasSymbolInBorder(prev, act, after, it.second, it.first)
        }
        .toList()
        .filter { it.second }
        .sumOf { it.first }
    }.sum())

}
