package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.KridExt.findByValue
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.step.Direction
import io.toolisticon.lib.krid.step

object _2023_10 {

  enum class Pipe(val value: Char, val directions: Pair<Direction, Direction>) {
    NS('|', Direction.UP to Direction.DOWN),
    EW('-', Direction.LEFT to Direction.RIGHT),
    NE('L', Direction.UP to Direction.RIGHT),
    NW('J', Direction.UP to Direction.LEFT),
    SE('F', Direction.DOWN to Direction.RIGHT),
    SW('7', Direction.LEFT to Direction.DOWN),
    ;

    companion object {
      fun valueOfChar(value: Char): Pipe = Pipe.entries.single { it.value == value }
    }

    private val dirs = listOf(directions.first, directions.second)

    fun next(dir: Direction): Direction = (dirs - dir.opposite).single()

    //S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
  }

  fun generate(content: String, startChar: Char): Sequence<Triple<Int, CellValue<Char>, Direction>> {
    val krid = Krids.krid(content)
    val s = krid.findByValue('S')

    return generateSequence(Triple(0, CellValue(s, '|'), Direction.UP)) { (i, cv, d) ->
      val enum = _2023_10.Pipe.valueOfChar(cv.value)
      val dir = enum.next(d)
      Triple(i + 1, krid.step(dir, cv.cell), dir)
    }
      .takeWhile { it.second.value != 'S' }
  }
}

fun main() {
  val input = AoCUtil.Input(year = 2023, day = 10, test = false, part = 1)

  val sequence = _2023_10.generate(input.contentRaw, '|')
    .map { it.second }
    //.map { it.copy(value = '*') }
    .toList()

  val k = Krids.krid(140,140,'.')
    .plus(sequence)

  println(k.ascii())






}
