package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil


fun main() {

  data class Cube(val x: Int, val y: Int, val z: Int) : Comparable<Cube> {
    override fun compareTo(other: Cube): Int {
      val a = this.x.compareTo(other.x)
      val b = this.y.compareTo(other.y)
      val c = this.z.compareTo(other.z)

      return if (a == 0) {
        if (b == 0) {
          c
        } else b
      } else a

    }

  }

  fun input(test: Boolean) = AoCUtil.Input(year = 2022, day = 18, test = test).nonEmptyLines.map {
    val (a,b,c) = it.split(",").map { it.trim().toInt() }
    Cube(a,b,c)
  }.sorted()


  fun part1(test: Boolean) : Int {
    return -1
  }

  println(input(true))
}
