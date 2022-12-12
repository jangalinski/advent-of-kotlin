package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.*
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.ORIGIN
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.CompositeStep
import io.toolisticon.lib.krid.model.step.Direction
import io.github.jangalinski.aoc.AoCUtil.Input

object Aoc2209 {
  fun findTMove(h: Cell, t: Cell, ignoreAdjacent: Boolean = false): Direction = if (!ignoreAdjacent && h isAdjacentOrEqual t) {
    // no need to move, we are close
    Direction.NONE
  } else if (h inSameColumn t) {
    // on same col - up or down?
    if (h.y > t.y) {
      Direction.DOWN
    } else {
      Direction.UP
    }
  } else if (h inSameRow t) {
    if (h.x > t.x) {
      Direction.RIGHT
    } else {
      Direction.LEFT
    }
  } else {
    findTMove(h, Krids.cell(t.x, h.y), true) combine findTMove(h, Krids.cell(h.x, t.y), true)
  }

}

fun main() {
  val input = Input("09-1.txt").nonEmptyLines.flatMap { line ->
    val (d, n_) = line.split(" ")
    val n = n_.toInt()
    when (d) {
      "U" -> List(n) { Direction.UP }
      "D" -> List(n) { Direction.DOWN }
      "L" -> List(n) { Direction.LEFT }
      else -> List(n) { Direction.RIGHT }
    }
  }

  fun List<Direction>.followerMoves(count: Int = 1): List<Direction> = if (count == 0) {
    this
  } else {
    sequence {
      var h = ORIGIN
      var t = ORIGIN

      forEach { hm ->
        h = hm(h)
        val tm = Aoc2209.findTMove(h, t)
        t = tm(t)
        yield(tm)
      }
    }.toList().followerMoves(count - 1)
  }

  fun List<Direction>.positions() = fold(CompositeStep(emptyList())) { c, n ->
    c + n
  }.walk(ORIGIN).toSet()

  fun part1(): Int = input.followerMoves().positions().size
  fun part2(): Int = input.followerMoves(9).positions().size

  println(part1())
  println(part2())
}
