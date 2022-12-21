package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.KridExt.findByValue
import io.github.jangalinski.aoc._2022.Aoc202214.Item.AIR
import io.github.jangalinski.aoc._2022.Aoc202214.line
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.getValue
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.Dimension
import io.toolisticon.lib.krid.model.step.Direction
import kotlin.math.max
import kotlin.math.min

object Aoc202214 {

  enum class Item(val char: Char, val blocked: Boolean) : () -> Char {
    SOURCE('+', false), SAND('o', true), AIR('.', false), ROCK('#', true);

    override fun invoke(): Char = char
  }

  fun Pair<Cell, Cell>.line(): List<Cell> = if (first.x == second.x) {
    (min(first.y, second.y)..max(first.y, second.y)).map { Cell(first.x, it) }
  } else {
    (min(first.x, second.x)..max(first.x, second.x)).map { Cell(it, first.y) }
  }

  enum class Result {
    NEXT, STOP, OUT
  }

}


fun main() {

  val input = AoCUtil.Input(
    year = 2022, day = 14,
    test = true
  ).nonEmptyLines
    .map {
      it.split(" -> ").map { c ->
        val (x, y) = c.split(",").map { n -> n.trim().toInt() }
        Cell(x, y)
      }
    }.let {
      val (min, max) = it.flatten().let { f ->
        listOf(
          Cell(f.minBy { c -> c.x }.x, f.minBy { c -> c.y }.y),
          Cell(f.maxBy { c -> c.x }.x, f.maxBy { c -> c.y }.y),
        )
      }
      (min to max) to it.map { l ->
        l.map { c -> Cell(c.x, c.y) }
      }.flatMap { l ->
        l.windowed(2) { m ->
          val (a, b) = m
          a to b
        }.flatMap { p -> p.line() }.toSet()
      }
    }.let {
      val krid: Krid<Aoc202214.Item> = Krids.krid(
        dimension = Dimension(it.first.second.x + 1, it.first.second.y + 1),
        emptyElement = AIR
      ) { x, y ->
        if (x == 500 && y == 0) {
          Aoc202214.Item.SOURCE
        } else if (it.second.contains(Cell(x, y))) {
          Aoc202214.Item.ROCK
        } else {
          AIR
        }
      }

      it.first to krid.subKrid(Cell(it.first.first.x, 0), Cell(krid.width - 1, krid.height - 1))
    }.second

  val source = input.findByValue(Aoc202214.Item.SOURCE)

  fun Cell.moves(): Triple<Cell, Cell, Cell> = Triple(Direction.DOWN(this), Direction.DOWN_LEFT(this), Direction.DOWN_RIGHT(this))

  fun Krid<Aoc202214.Item>.move(cell: Cell): Pair<Cell, Aoc202214.Result> {
    val (mu,mul, mur) = cell.moves()
//
//
//
//
//    val (vu, vul, vur)  = Triple(this.getValue(mu), this.getValue(mul), this.getValue(mur))
//
//    val bounds =
//      Triple(this.dimension.isInBounds(mu), this.dimension.isInBounds(mul), this.dimension.isInBounds(mur))
//
//    // we can move down
//    if (vu.value == AIR) {
//      return move(vu.cell)
//    }
//    // we can
//    else if (next.first.value == AIR) {
//      return move(next.second.cell)
//    } else if (next.third.value == AIR) {
//      return move(next.second.cell)
//    } else if (next.first.value.blocked) {
//      if (next.second.value.blocked) {
//
//      } else {
//        return move(next.second.cell)
//      }
//    }
    TODO()
  }

  fun part1(): Int {
    var k = input.copy()





    return -1
  }

  fun part2(): Int {
    return -1
  }

  println(input.ascii { it() })
}
