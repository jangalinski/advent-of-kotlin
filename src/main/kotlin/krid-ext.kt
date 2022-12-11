package io.github.jangalinski.aoc

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.fn.IndexTransformer
import io.toolisticon.lib.krid.model.*
import io.toolisticon.lib.krid.model.step.Direction

fun <E : Any> Row<E>.cellValues(): List<CellValue<E>> = values.mapIndexed { x, v -> CellValue<E>(x, index, v) }
fun <E : Any> Column<E>.cellValues(): List<CellValue<E>> = values.mapIndexed { y, v -> CellValue<E>(index, y, v) }

fun <E : Any> krid(dimension: Dimension, emptyValue: E, cellValues: List<CellValue<E>>): Krid<E> {
  return Krids.krid(
    dimension = dimension,
    emptyElement = emptyValue,
    initialize = { x, y ->
      cellValues.first { it.x == x && it.y == y }.value
    }
  )
}

val Krid<*>.indexTransformer: IndexTransformer get() = IndexTransformer(this.width)

//fun <E:Any> krid(krid : Krid<E>,)

infix fun Cell.inSameRow(b: Cell) = y == b.y
infix fun Cell.inSameColumn(b: Cell) = x == b.x

infix fun Cell.isAdjacent(other: Cell) = adjacent.contains(other)
infix fun Cell.isAdjacentOrEqual(other: Cell) = this == other || adjacent.contains(other)

/**
 * Only combine UP/DOWN with LEFT/RIGHT (and NONE)
 */
infix fun Direction.combine(other: Direction): Direction = when (this) {
  Direction.NONE -> other
  Direction.UP -> when (other) {
    Direction.RIGHT -> Direction.UP_RIGHT
    Direction.LEFT -> Direction.UP_LEFT
    Direction.NONE -> this
    else -> throw IllegalArgumentException("$this can only be combined with LEFT/RIGHT/NONE.")
  }

  Direction.DOWN -> when (other) {
    Direction.LEFT -> Direction.DOWN_LEFT
    Direction.RIGHT -> Direction.DOWN_RIGHT
    Direction.NONE -> this
    else -> throw IllegalArgumentException("$this can only be combined with LEFT/RIGHT/NONE.")
  }

  Direction.RIGHT -> when (other) {
    Direction.UP -> Direction.UP_RIGHT
    Direction.DOWN -> Direction.DOWN_RIGHT
    Direction.NONE -> this
    else -> throw IllegalArgumentException("$this can only be combined with UP/DOWN/NONE.")
  }

  Direction.LEFT -> when (other) {
    Direction.UP -> Direction.UP_LEFT
    Direction.DOWN -> Direction.DOWN_LEFT
    Direction.NONE -> this
    else -> throw IllegalArgumentException("$this can only be combined with UP/DOWN/NONE.")
  }

  else -> throw IllegalArgumentException("combine only works with vertical + horizontal, no mix allowed.")
}
