package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.ListExt.peekPrint
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.ascii

fun main() {
  fun Krid<*>.equalRows(i1: Int, i2: Int): Boolean = (this.row(i1).values == this.row(i2).values)
  fun Krid<*>.equalCols(i1: Int, i2: Int): Boolean = (this.column(i1).values == this.column(i2).values)



  fun Krid<Char>.colCandidates() =
    columns().windowed(2).map { (a, b) -> a.index to equalCols(a.index, b.index) }.filter { it.second }.map { it.first }

  fun Krid<Char>.rowCandidates() =
    rows().windowed(2).map { (a, b) -> a.index to equalRows(a.index, b.index) }.filter { it.second }.map { it.first }

  fun Krid<Char>.candidates() = colCandidates() to rowCandidates()

  fun Krid<Char>.isColMirror(index: Int): Boolean = generateSequence(index -1 to index +2) { it.first - 1 to it.second + 1 }
    .takeWhile { this.dimension.columnRange.contains(it.first) && dimension.columnRange.contains(it.second) }
    .all { this.equalCols(it.first, it.second) }

  fun Krid<Char>.isRowMirror(index: Int): Boolean = generateSequence(index to index + 1) { it.first - 1 to it.second + 1 }
    .takeWhile { this.dimension.rowRange.contains(it.first) && dimension.rowRange.contains(it.second) }
    .all { this.equalRows(it.first, it.second) }

  fun Krid<Char>.mirrors(): Pair<List<Int>, List<Int>> {
    val (cols, rows) = this.candidates()
    return cols.filter { this.isColMirror(it) }.map { it +1 } to rows.filter { this.isRowMirror(it) }.map { it +1 }
  }

  val input = AoCUtil.Input(year = 2023, day = 13, test = false).linesChunkedByEmpty()
    .map { Krids.krid(it.joinToString(separator = "\n")) }

  input.map {
    it.mirrors()
  }.fold(0 to 0) {(ac,ar),(cc,cr) ->
    ac + cc.sum() to ar + (cr.sum() * 100)
  }.let { it.first + it.second }
    .also { println(it) }

}
