package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.ListExt.peekPrint
import io.github.jangalinski.aoc.AoCUtil.StringExt.toPair


enum class PokerHand {
  HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OK, FULL_HOUSE, FOUR_OK, FIVE_OK
}

fun main() {

  /**
   * 0 - High Card (....)
   * 1 - One Pair (x,2 - (r,1))
   * 2 - Two Pair (x,2 - y,2 - z,1)
   * 3 - Three of kind (x,3 - y,1 - z,1)
   * 4 - Full House (x,3 - y,2)
   * 5 - Four Kind (x,4 - y,1)
   * 6 - Five Kind (x,5)
   */
  fun hand(h: Map<Int, Int>, jackIsJoker: Boolean): Int {
    val hand = if (jackIsJoker && h.keys.contains(1)) {
      if (h.size == 1) {
        h
      } else {
        val m = h.toMutableMap()
        val j = m.remove(1)!!

        val max = m.entries.sortedByDescending { it.value }.first()
        m.put(max.key, max.value + j)
        m.toMap()
      }
    } else {
      h
    }


    return when (hand.size) {
      5 -> 0
      1 -> 6
      4 -> 1
      3 -> if (hand.values.max() == 2) {
        2
      } else {
        3
      }

      else -> if (hand.values.max() == 4) {
        5
      } else {
        4
      }
    }
  }

  fun List<Int>.concat(): Long = joinToString(separator = "") { it.toString().padStart(2, '0') }.toLong()

  fun parse(line: String, jackIsJoker: Boolean): Triple<String, Int, Pair<Long, Int>> {
    val pair = line.toPair(" ")
    val hand = pair.first
    val value = pair.second.toInt()

    val handToInt = hand.map {
      when (it) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> if (jackIsJoker) 1 else 11
        'T' -> 10
        else -> it.digitToInt()
      }
    }
    val hash = handToInt.concat()
    val count = handToInt.groupingBy { it }.eachCount()
    val handResult = hand(count, jackIsJoker)

    return Triple(hand, value, hash to handResult)
  }


  fun parse(input: AoCUtil.Input, jackIsJoker: Boolean): List<Triple<String, Int, Pair<Long, Int>>> = input.nonEmptyLines
    .map {
      parse(it, jackIsJoker)
    }

  val comp = Comparator<Triple<String, Int, Pair<Long, Int>>> { o1, o2 ->
    val byHand = o1!!.third.second.compareTo(o2!!.third.second)

    if (byHand == 0) {
      o1.third.first.compareTo(o2.third.first)
    } else {
      byHand
    }
  }


  val input = parse(
    AoCUtil.Input(
      year = 2023,
      day = 7,
      test = false
    ), true
  )

  val sorted = input.sortedWith(comp)

  val i2 = parse(
    AoCUtil.Input(
      year = 2023,
      day = 7,
      test = false
    ), false
  )

  println(sorted.mapIndexed { i, v -> (i + 1) * v.second }.sum())
  val m1 = input.associateBy { it.first }
  val m2 = i2.associateBy { it.first }

  m1.keys.filter { it.contains("J") }.map {
    m1[it]!! to m2[it]!!
  }.map {
    val p1 = PokerHand.entries[it.first.third.second]
    val p2 = PokerHand.entries[it.second.third.second]

    Triple(it.first.first, p1 to p2, p1 > p2)
  }.filterNot { it.third }
    .peekPrint()
}
