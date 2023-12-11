package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.ListExt.head
import io.toolisticon.lib.krid.Krid
import kotlin.math.abs

fun main() {

  fun sumOfDistance(krid: Krid<Char>, factor: Int): Long {
    fun List<Char>.empty() = all { it == '.' }

    val emptyRows = krid.rows().filter { it.empty() }.map { it.index.toLong() }
    val emptyCols = krid.columns().filter { it.empty() }.map { it.index.toLong() }

    // find all galaxies and alter their coordinates according to the factor
    val galaxies = krid.cellValues().filter { it.value == '#' }.map { it.x.toLong() to it.y.toLong() }
      .map { (x, y) ->
        x + (emptyCols.count { it < x } * factor) to y + (emptyRows.count { it < y } * factor)
      }
      .toList()

    val pairs = galaxies.fold(listOf<Pair<Pair<Long, Long>, Pair<Long, Long>>>() to galaxies) { (result, remaining), _ ->
      val (head, rest) = remaining.head()
      val pairs = rest.map { head to it }

      (result + pairs) to rest
    }.first

    return pairs.sumOf { (a, b) -> abs(a.first - b.first) + abs(a.second - b.second) }
  }


  val k = AoCUtil.Input(year = 2023, day = 11, test = false).krid()
  println(sumOfDistance(k, 999_999))
}
