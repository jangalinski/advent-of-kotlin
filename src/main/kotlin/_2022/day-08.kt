package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil.Input
import io.github.jangalinski.aoc._2022.Aoc2208.toTreeView
import io.github.jangalinski.aoc.cellValues
import io.github.jangalinski.aoc.krid
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.Column
import io.toolisticon.lib.krid.model.Row
import io.toolisticon.lib.krid.toAddKrid

object Aoc2208 {

  data class TreeView(
    val cellValue: CellValue<Int>,
    val left: List<Int>,
    val right: List<Int>,
    val up: List<Int>,
    val down: List<Int>
  ) {


    val visibleLeft: List<Int> = left.filterVisible(cellValue.value)
    val visibleRight: List<Int> = right.filterVisible(cellValue.value)
    val visibleUp: List<Int> = up.filterVisible(cellValue.value)
    val visibleDown: List<Int> = down.filterVisible(cellValue.value)

    val scenicScore = visibleLeft.size * visibleRight.size * visibleUp.size * visibleDown.size
    override fun toString(): String {
      return """TreeView(cellValue=$cellValue,
        |left=$left,
        |right=$right,
        |up=$up,
        |down=$down,
        |visibleLeft=$visibleLeft,
        |visibleRight=$visibleRight,
        |visibleUp=$visibleUp,
        |visibleDown=$visibleDown,
        |scenicScore=$scenicScore
        |)""".trimMargin()
    }
  }

  fun forest(file: String) = Input(file)
    .digitKrid()

  fun Krid<Int>.toTreeView(x: Int, y: Int): TreeView {
    val treeHeight = this[x, y]
    val row = this.row(x)
    val col = this.column(y)
    return TreeView(
      cellValue = cell(x, y, treeHeight),
      left = row.subList(0, y).reversed(),
      right = row.subList(y + 1, row.lastIndex + 1),
      up = col.subList(0, x).reversed(),
      down = col.subList(x + 1, col.lastIndex + 1)
    )
  }

  fun Krid<Int>.toTreeView(): List<TreeView> {
    return iterator().asSequence().map { (x, y, _) ->
      toTreeView(x, y)
    }.toList()
  }


  fun List<Int>.filterVisible(value: Int = -1): List<Int> {
    if (value == -1) {
      return this.fold(-1 to emptyList<Int>()) { (max, list), next ->
        if (next >= max) {
          next to list + next
        } else {
          max to list
        }
      }.second
    }

    if (isEmpty()) {
      return emptyList()
    }

    val first = first()
    val visible = this.filterVisible()

    return if (visible.all { it >= value }) {
      visible
    } else if (first > value) {
      listOf(first)
    } else {
      visible.fold(value to emptyList<Int>()) { (max, list), next ->
        if (max > next) {
          max to list + next
        } else if (max < next) {
          next to list
        } else {
          if (value >= next) {
            max to list + next
          } else {
            max to list
          }
        }
      }.second
    }
  }
}

fun main() {
  val toChar: (Boolean) -> Char = { e -> if (e) '#' else '.' }

  val forest = Aoc2208.forest("08-1-test.txt")

  fun part1(): Int {
    var visible = Krids.krid(forest.width, forest.height, false) { _, _ -> false }

    fun List<Int>.look(): List<Boolean> {
      require(size > 1)
      var max = -1
      return map {
        val visible = it > max
        if (visible) {
          max = it
        }
        visible
      }
    }

    fun List<Int>.lookBidir(): List<Boolean> {
      val right = this.look()
      val left = this.reversed().look().reversed()

      return left.zip(right)
        .map { it.first || it.second }
    }

    val rowView = forest.rows().flatMap { (i, v) ->
      Row(i, v.lookBidir()).cellValues()
    }.let { krid(visible.dimension, false, it) }

    val colView = forest.columns().flatMap { (i, v) ->
      Column(i, v.lookBidir()).cellValues()
    }.let { krid(dimension = visible.dimension, emptyValue = false, cellValues = it) }

    visible = visible.plus(rowView.toAddKrid { b1, b2 -> b1 || b2 })
    visible = visible.plus(colView.toAddKrid { b1, b2 -> b1 || b2 })

    return visible.rows().flatMap { it.values }.count { it }
  }

  fun part2(): Int = forest.toTreeView().maxOf { it.scenicScore }




  println(part1())
  println(part2())

}
