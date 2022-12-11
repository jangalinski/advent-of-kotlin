package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.Input
import io.github.jangalinski.aoc.indexTransformer
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.plus


fun main() {
  val input = Input("10-1.txt").nonEmptyLines
    .map {
      if (it == "noop") {
        1 to 0
      } else {
        2 to it.removePrefix("addx ").toInt()
      }
    }

  fun part1() = sequence<Int> {
    var x = 1
    var cycles = 0

    input.forEach { (c, n) ->
      repeat(c) {
        cycles += 1
        if (cycles - 20 == 0 || (cycles - 20) % 40 == 0) {
          yield(cycles * x)
        }
      }
      x += n
    }
  }.sum()

  data class X(val value: Int) {

    val krid = Krids.krid(40, 1, '.') { x, _ -> if (x == value - 1 || x == value || x == value + 1) '#' else '.' }

    operator fun plus(num: Int): X = copy(value = value + num)

    operator fun get(cycle: Int): Char = krid[cycle - 1, 0]

    override fun toString() = "X=$value, line='${krid.ascii()}'"
  }

  fun part2(): String {
    var crt: Krid<Char> = Krids.krid(width = 40, height = 6, emptyElement = ' ')
    val cell: (Int) -> Cell = { crt.indexTransformer.toCell(it - 1) }
    var x = X(1)
    var cycles = 0

    input.forEach { (c, n) ->
      repeat(c) {
        cycles += 1
        val cell = cell(cycles)
        crt = crt.plus(CellValue(cell, x[cell.x + 1]))
      }
      x += n
    }

    return crt.ascii()
  }

  //println(part1())

  println(part2())
}
