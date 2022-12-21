package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc._2022.Aoc202215.manhattan
import io.toolisticon.lib.krid.model.Cell
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Aoc202215 {
  val NO_LIMIT = Integer.MIN_VALUE to Integer.MAX_VALUE
  fun input(test: Boolean): List<Pair<Cell, Cell>> = AoCUtil.Input(
    year = 2022,
    day = 15,
    test = test
  ).nonEmptyLines.map { line ->
    val p = line.replace("Sensor at ", "")
      .replace(" closest beacon is at ", "")
      .replace(" ", "")
      .replace("x=", "")
      .replace("y=", "")

    fun toCell(s: String): Cell {
      val (x, y) = s.split(",").map { it.trim().toInt() }
      return Cell(x, y)
    }

    val (s, b) = p.split(":").map { toCell(it) }
    s to b
  }

  data class Sensor(
    val cell: Cell,
    val radius: Int,
    val limit: Pair<Int, Int> = Integer.MIN_VALUE to Integer.MAX_VALUE
  ) {

    fun inRow(y: Int): List<Cell> {
      val distance = abs(cell.y - y)

      return if (distance > radius) {
        emptyList()
      } else {
        val length = radius - distance


        val left = cell.x - length
        val right = cell.x + length


        (left..right).map { Cell(it, y) }
      }
    }

    fun rangeInRow(y: Int, limit: Pair<Int, Int> = NO_LIMIT): IntRange? {
      val distance = abs(cell.y - y)

      return if (distance > radius) {
        null
      } else {
        val length = radius - distance


        val left = max(cell.x - length, limit.first)
        val right = min(cell.x + length, limit.second)

        left..right
      }
    }
  }

  fun Pair<Cell, Cell>.manhattan() = Sensor(
    cell = first,
    radius = abs(first.x - second.x) + abs(first.y - second.y)
  )

  fun List<IntRange>.reduce(limit: Pair<Int, Int> = NO_LIMIT): List<IntRange> {
    val all = limit.first..limit.second

    if (this.contains(limit.first..limit.second)) {
      return listOf(limit.first..limit.second)
    } else {
      return this.fold(mutableListOf<IntRange>()) { c, n ->
        if (c.isEmpty()) {
          c + n
        } else {

        }

        c
      }

    }
  }
}

fun main() {

  fun part1(test: Boolean): Int {
    val y = if (test) 10 else 2_000_000

    val input = Aoc202215.input(test)

    val sensors = input.map { it.manhattan() }

    val allSensors = sensors.map { it.cell }.filter { it.y == y }.toSet()
    val allBeacons = input.map { it.second }.filter { it.y == y }.toSet()

    val allCovered = sensors.flatMap {
      it.inRow(y)
    }.distinct()

    println(allSensors)
    println(allBeacons)
    println(allCovered)


    return (allCovered - allBeacons - allSensors).size
  }


  fun part2(test: Boolean): Int {
    val limit = 0 to if (test) 20 else 4_000_000


    val input = Aoc202215.input(test)

    val sensors = input.map { it.manhattan() }

    val allSensors = sensors.map { it.cell }.filter { it.x in (limit.first..limit.second) }.toSet()
    val allBeacons = input.map { it.second }.filter { it.y in (limit.first..limit.second) }.toSet()


    val allCovered = (limit.first..limit.second)
      .map { y -> sensors.map { s -> s.rangeInRow(y, limit) to y } }

      .map {
        it.filter { p -> p.first != IntRange.EMPTY }
      }
      .toSet()

    println(allCovered)

    return allCovered.size
  }

  println(part2(true))


}
