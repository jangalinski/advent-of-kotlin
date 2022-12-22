package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.KridExt.table
import io.github.jangalinski.aoc._2022.Aoc202222.print
import io.github.jangalinski.aoc._2022.Aoc202222.walk
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.get
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.Direction
import io.toolisticon.lib.krid.model.step.Direction.*

object Aoc202222 {

  fun String.extractMoves(): List<Direction> {
    val mapNotNull = replace("""([LR])""".toRegex(), ",$1,").split(",")
      .map { it.trim() }
      .mapNotNull { it.ifEmpty { null } }

    fun Direction.turn(s: String): Direction = when (this) {
      UP -> when (s) {
        "L" -> LEFT
        else -> RIGHT
      }

      RIGHT -> when (s) {
        "L" -> UP
        else -> DOWN
      }

      DOWN -> when (s) {
        "L" -> RIGHT
        else -> LEFT
      }

      else -> when (s) {
        "L" -> DOWN
        else -> UP
      }
    }

    return sequence {
      var direction = RIGHT

      mapNotNull.forEach {
        val i = it.toIntOrNull()

        if (i == null) {
          direction = direction.turn(it)
        } else {
          repeat(i) {
            yield(direction)
          }
        }

      }

    }.toList()
  }

  fun Krid<Boolean?>.walk(start: Cell, direction: Direction): Cell {
    var target = start(direction(1))

    fun wrapAround(c: Cell, direction: Direction): Cell = when (direction) {
      UP -> Cell(c.x, this.column(c.x).indexOfLast { it != null })
      RIGHT -> Cell(this.row(c.y).indexOfFirst { it != null }, c.y)
      DOWN -> Cell(c.x, this.column(c.x).indexOfFirst { it != null })
      else -> Cell(this.row(c.y).indexOfLast { it != null }, c.y)
    }

    var value = if (!this.dimension.isInBounds(target)) {
      null
    } else {
      this[target]
    }

    if (true == value) {
      return target
    } else if (false == value) {
      return start
    }

    target = wrapAround(start, direction)
    val x = true == this[target]

    return if (true == x) {
      target
    } else {
      start
    }

    return Cell(0, 0)
  }

  fun Krid<Boolean?>.print() = this.table {
    when (it) {
      true -> "O"
      false -> "X"
      else -> " "
    }
  }

  fun input(test: Boolean): Pair<Krid<Boolean?>, List<Direction>> {
    val string = AoCUtil.Input(year = 2022, day = 22, test = test).contentRaw

    val (k, path) = string.split("\n\n")

    val max = k.lines().map { it.length }.max()
    val l = k.lines().joinToString("\n") { it.padEnd(max) }
      .replace(" ", "n")
      .replace(".", "t")
      .replace("#", "f")


    val kr: Krid<Boolean?> = Krids.krid(string = l, emptyElement = null) { c ->
      when (c) {
        't' -> true
        'f' -> false
        else -> null
      }
    }


    return kr to path.extractMoves()
  }

}

fun main() {


  fun part1(test: Boolean): Int {
    val (krid, directions) = Aoc202222.input(test)

    var cell = cell(krid.row(0).indexOfFirst { true == it }, 0)

    val facing = when(directions.last()) {
      UP -> 3
      RIGHT -> 0
      DOWN -> 1
      else -> 2
    }

    directions.forEach {
      cell = krid.walk(cell, it)
    }


//
//    To finish providing the password to this strange input device, you need to determine numbers for your final row,
//    column, and facing as your final position appears from the perspective of the original map.
//    Rows start from 1 at the top and count downward; columns start from 1 at the left and count rightward.
//    (In the above example, row 1, column 1 refers to the empty space with no tile on it in the top-left corner.)
//    Facing is 0 for right (>), 1 for down (v), 2 for left (<), and 3 for up (^). The final password is the sum of 1000 times the row, 4 times the column, and the facing.
//
//    In the above example, the final row is 6, the final column is 8, and the final facing is 0. So, the final password is 1000 * 6 + 4 * 8 + 0: 6032.
//
//    Follow the path given in the monkeys' notes. What is the final password?


    return ((cell.y+1) * 1000) + ((cell.x+1)*4) + facing
  }

  println(part1(false))
}
