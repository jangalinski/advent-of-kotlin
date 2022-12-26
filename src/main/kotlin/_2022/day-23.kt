package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.KridExt.adjacentInDir
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.Direction

fun main() {
  fun input(test: Boolean): List<Cell> = AoCUtil.Input(year = 2022, day = 23, test = test).contentRaw.let {
    Krids.krid(it, '.').iterator().asSequence()
  }.filter {
    it.value == '#'
  }.map {
    it.cell
  }.toList()

  fun List<Cell>.planMove(): List<Pair<Cell, Cell?>> {
    fun Cell.toNorth(): List<Cell> = this.adjacentInDir(Direction.UP_LEFT, Direction.UP, Direction.UP_RIGHT)
    fun Cell.toSouth(): List<Cell> = this.adjacentInDir(Direction.DOWN_LEFT, Direction.DOWN, Direction.DOWN_RIGHT)
    fun Cell.toWest(): List<Cell> = this.adjacentInDir(Direction.UP_LEFT, Direction.LEFT, Direction.DOWN_LEFT)
    fun Cell.toEast(): List<Cell> = this.adjacentInDir(Direction.UP_RIGHT, Direction.RIGHT, Direction.DOWN_RIGHT)

    return map { cell ->
      val neighbors = cell.adjacent.filter { this.contains(it) }.toSet()
      if (cell.toNorth().intersect(neighbors).isEmpty()) {
        cell to Direction.UP(cell)
      } else if (cell.toSouth().intersect(neighbors).isEmpty()) {
        cell to Direction.DOWN(cell)
      } else if (cell.toWest().intersect(neighbors).isEmpty()) {
        cell to Direction.LEFT(cell)
      } else if (cell.toEast().intersect(neighbors).isEmpty()) {
        cell to Direction.RIGHT(cell)
      } else {
        cell to null
      }
    }
  }

  fun List<Pair<Cell, Cell?>>.move(): List<Cell> {
    val available = this.mapNotNull { it.second }.groupingBy { it }.eachCount()
      .filter { it.value == 1 }.map { it.key }.toSet()
    return this.map { (cell, dest) ->
      if (dest != null && available.contains(dest)) {
        dest
      } else {
        cell
      }
    }
  }

  fun part1(test: Boolean): Int {
    var cells = input(true)

    repeat(10) {
      val moves = cells.planMove()
      println("--")
      println(cells)
      println(moves)
      println("--")
      cells = moves.move()
    }

    val width = cells.map { it.x }.sorted().let { it.last() - it.first() }
    val height = cells.map { it.y }.sorted().let { it.last() - it.first() }

    println(cells)

    return (width * height) - cells.size
  }

  println(part1(true))
}
