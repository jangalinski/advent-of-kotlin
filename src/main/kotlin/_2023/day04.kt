package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.StringExt.toPair
import kotlin.math.pow

fun main() {
  fun parse(line: String): Triple<Int, List<Int>, List<Int>> {
    val (c, r) = line.toPair(":")
    val index = c.split(" ").last().trim().toInt()
    val (w, n) = r.toPair(" | ")

    fun numbers(sub: String) = sub.trim().split(" ").flatMap { it.trim().split(" ") }.filterNot { it.isEmpty() }.map { it.toInt() }

    return Triple(index, numbers(w), numbers(n))
  }


  fun Triple<Int, List<Int>, List<Int>>.matching(): Int = this.second.intersect(this.third).size
  fun Triple<Int, List<Int>, List<Int>>.silver(): Int {
    val intersect = matching()
    return if (intersect == 0) 0 else 2.toDouble().pow((intersect - 1).toDouble()).toInt()
  }

  val input = AoCUtil.Input(2023, 4, test = false)


  val inputGold = input.nonEmptyLines.map { parse(it) }.associate { it.first to (it.matching() to 1) }.toMutableMap()

  println(sequence<Int> {
    (1..inputGold.keys.max()).forEach { card ->
      val (win, count) = inputGold.remove(card)!!

      if (win > 0) {
        (card + 1..card + win).forEach { k ->
          inputGold.computeIfPresent(k) { _, v ->
            (v.first) to (v.second + count)
          }
        }
      }

      yield(count)
    }
  }.sum())
//  var sum = 0
//  var p = inputGold.head()
//  while (p.second.isNotEmpty()) {
//    println("sum=${sum.toString().padStart(10,' ')} - $p")
//    val win = p.first.first
//    sum += p.first.second
//
//    val x = p.second.toMutableList().apply {
//
//
//    }
//
//
//
//    p = x.head()
//
//  }

}
