package io.github.jangalinski.aoc._2023

import com.ibm.icu.text.RuleBasedNumberFormat
import io.github.jangalinski.aoc.AoCUtil.Input
import java.util.*

fun main() {

  fun silver(input: Input): Int {
    return input.nonEmptyLines.map {
      it.replace("""[a-z]""".toRegex(), "")
    }
      .map {
        "${it.first()}${it.last()}"
      }.sumOf { it.toInt() }
  }

  fun gold(input: Input): Int {
    val number = RuleBasedNumberFormat(Locale.ENGLISH, RuleBasedNumberFormat.SPELLOUT)
    val digits = (1..9).associate { number.format(it) to it }

    // returns sorted list of digits, no matter if word or number
    fun String.findAllDigits(): List<Int> = digits
      .flatMap { (d, n) ->
        Regex("$d|$n").findAll(this)
          .map { it.range.first }
          .toList()
          .map {
            n to it
          }
      }
      .sortedBy { it.second }
      .map { it.first }


    return input.nonEmptyLines.map {
      val d = it.findAllDigits()
      d.first() * 10 + d.last()
    }.sum()
  }


  //println(silver(Input(year = 2023, day = 1, test = false, part = 1)))
  println(gold(Input(year = 2023, day = 1, test = false, part = 1)))

}
