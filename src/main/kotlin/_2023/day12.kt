package io.github.jangalinski.aoc._2023

import com.ibm.icu.text.PersonNameFormatter.Length
import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.ListExt.head
import io.github.jangalinski.aoc.AoCUtil.ListExt.peekPrint
import io.github.jangalinski.aoc.AoCUtil.ListExt.tail
import io.github.jangalinski.aoc.AoCUtil.StringExt.splitTrimmed
import io.github.jangalinski.aoc.AoCUtil.StringExt.toPair

fun main() {

  fun replaceWith(length: Int): Sequence<String> = generateSequence(0) {it +1}.map {
    Integer.toBinaryString(it).padStart(length,'.')
      .replace('0','.')
      .replace('1','#')
  }.takeWhile { it.length <= length }

  fun mergeWith(string: String, bits: String): String {
    var c = 0
    return string.split("?").map {
      it.ifEmpty {
        bits[c].also {
          c += 1
        }
      }
    }.joinToString(separator = "")
  }

  fun allVariants(string: String): Sequence<String> = replaceWith(string.count { it=='?' })
    .map {bits ->
      string.fold("" to bits.toList()) {(result, remainingBits), nextChar ->
        if (nextChar != '?') {
          result + nextChar to remainingBits
        } else {
          val (nextBit, r) = remainingBits.head()

          result + nextBit to r
        }
      }.first

    }


  // true if # blocks match list
  fun String.validate(expected: List<Int>): Boolean {
    val groups = this.splitTrimmed(".").map { it.length }

    return groups.size == expected.size && groups.zip(expected).all { (a,b) -> a == b }
  }

  fun silver(line: String): Int {
    val (s,c) = line.trim().toPair(" ").let {
      it.first  to it.second.split(",").map { it.toInt() }
    }

    val v = allVariants(s)

    return v
      .filter { it.validate(c) }
      .count()
  }

  val input = AoCUtil.Input(year = 2023, day=12, test = false)

  println(input.nonEmptyLines.map { silver(it) }
    .sum())



}
