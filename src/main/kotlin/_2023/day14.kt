package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.ListExt.peekPrint
import io.github.jangalinski.aoc._2023.Day14.PlatformItem.CUBE
import io.github.jangalinski.aoc._2023.Day14.PlatformItem.ROUND
import io.github.jangalinski.aoc.krid
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.Column
import io.toolisticon.lib.krid.model.Columns
import io.toolisticon.lib.krid.toAddKrid

object Day14 {

  interface CubeFinder {
    fun inRow(index:Int) : List<Int>
    fun inColumn(index:Int) : List<Int>
  }

  data class Platform(
    private val krid: Krid<PlatformItem?>,
    private val cubes: CubeFinder,
  ) {

    constructor(krid: Krid<PlatformItem?>) : this(
      krid = krid,
      cubes = krid.cellValues().filter { it.value == CUBE }.map { it.cell }.toList().let {cells ->
        object : CubeFinder{
          private val inRow: MutableMap<Int, List<Int>> = mutableMapOf()
          private val inCol: MutableMap<Int, List<Int>> = mutableMapOf()
          override fun inRow(index: Int): List<Int> = inRow.computeIfAbsent(index) { _ -> cells.filter { it.x == index }.map { it.y }}

          override fun inColumn(index: Int): List<Int> = inCol.computeIfAbsent(index) {_-> cells.filter { it.y  == index}.map { it.x }}
        }
      }
    )

    fun cubesInRow(index: Int) = cubes.inRow(index)
    fun cubesInCol(index: Int) = cubes.inColumn(index)

    constructor(string: String) : this(Krids.krid(string = string, emptyElement = null, parse = PlatformItem::valueOfChar))

    fun ascii() = krid.ascii { it?.char ?: '.' }

    val load by lazy {
      krid.rows().sumOf { (krid.height - it.index) * it.values.count { it == ROUND } }
    }

    fun tiltRow(index: Int, left: Boolean) {
      val cubes = cubesInRow(index)
      val row = krid.row(index).values
      val chunks = cubes.fold(0 to mutableListOf<List<PlatformItem?>>()) { a, index ->
        val sub: List<PlatformItem?> = row.subList(a.first, index)
        index to a.second.apply { add(sub) }
      }.second.peekPrint()
    }

    fun tiltNorth(): Platform {

      return this
    }
  }

  enum class PlatformItem(val char: Char) {
    CUBE('#'),
    ROUND('O'),
    ;

    companion object {
      private val byChar = entries.associateBy { it.char }

      fun valueOfChar(char: Char): PlatformItem? = byChar[char]
    }
  }

  fun platform(test: Boolean) = platform(AoCUtil.Input(year = 2023, day = 14, test = test))

  fun platform(input: AoCUtil.Input): Platform =
    Platform(Krids.krid(string = input.contentTrimmed, emptyElement = null, parse = PlatformItem::valueOfChar))

}

fun main() {


  val input = AoCUtil.Input(year = 2023, day = 14, test = true).krid()
    .let {
      Krids.krid(it.width + 2, it.height + 2, '.') { _, _ -> '#' } + it.toAddKrid(Cell(1, 1))
    }

//  // silver
//  input.columns()
//    .filter { it.values.contains('O') }
//    .map { it.values.joinToString(separator = "") }
//    .map { col ->
//      col to col.mapIndexed { index, c -> index to (c == '#') }.filter { it.second }.map { it.first }
//        .windowed(2)
//        .map { (a, b) -> a to col.substring(a + 1, b) }
//        //.filter { it.second.isNotEmpty() }
//        .map { it.first to (it.second.replace(".","").padEnd(it.second.length, '.')) }
//        .fold("") {a,c ->
//
//          a + "#" + c.second
//        }
//    }
//    .let { Krids.krid(it.joinToString("\n") {it.second}) }
//    .columns().map { it.index to it.values.count { it == 'O' } }.let { it.map { it.second }.zip(it.reversed().map { it.first +1 }) }
//    .sumOf { it.first * it.second }
//    .also { println(it) }

  val compareElements: Comparator<Char> = Comparator { o1, o2 ->
    when (o1) {
      '.' -> when (o2) {
        '.' -> 0
        '#' -> -1
        'O' -> 1
        else -> 0
      }

      'O' -> when (o2) {
        '.' -> 0
        '#' -> -1
        'O' -> 1
        else -> 0
      }

      '#' -> when (o2) {
        '.' -> 0
        '#' -> -1
        'O' -> 1
        else -> 0
      }

      else -> 0
    }
  }


  val p = Day14.Platform("""
    O..#O
    #.OO#
    O.##O
  """.trimIndent()).tiltRow(1, false)
}
