package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.ListExt.peekPrint

fun main() {


  fun hash(current: Int, char: Char): Int {
    var c = current + char.code
    c *= 17
    return c % 256
  }

  fun hash(string: String): Int = string.asSequence().fold(0) { a, c -> hash(a, c) }

  fun silver(input: AoCUtil.Input): Int = input.contentTrimmed.split(",").map { it.trim() }.asSequence()
    .map { hash(it) }
    .sum()

  fun gold(input: AoCUtil.Input): Int {
    val boxes = buildList<LinkedHashMap<String, Int>> {
      (0..255).forEach { _ ->
        add(LinkedHashMap())
      }
    }

    input.contentTrimmed.split(",").map { it.trim() }.asSequence()
      .map {
        val (s, f) = it.split("""[=-]""".toRegex(), 2)
        Triple(s, hash(s), if (f.isEmpty()) -1 else f.toInt())
      }.fold(boxes) { b, t ->
        val map = b[t.second]
        // shall add
        if (t.third >= 0) {
          map.compute(t.first) { _, v ->
            t.third
          }
        }
        // shall remove
        else {
          map.remove(t.first)
        }

        b
      }

    val r = boxes.flatMapIndexed { index, map ->
      map.values.mapIndexed() { i,n -> Triple(index+1, i+1, n) }
    }.map {
      it.first * it.second * it.third
    }

     return r.sum()
  }

  val input = AoCUtil.Input(year = 2023, day = 15, test = false)
  println(silver(input))
  println(gold(input))
}
