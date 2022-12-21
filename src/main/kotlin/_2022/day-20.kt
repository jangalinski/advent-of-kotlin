package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc._2022.Aoc202220.N
import io.github.jangalinski.aoc._2022.Aoc202220.input

object Aoc202220 {

  data class N(val index: Int, val value: Int) {
    override fun toString() = "($index,$value)"
  }

  fun List<*>.newIndex(oldIndex: Int, move: Int): Int = effectiveIndex(oldIndex + move)


  fun List<*>.effectiveIndex(index: Int): Int = if (index in (indices)) {
    index
  } else if (index < 0) {
    effectiveIndex(index + size)
  } else {
    effectiveIndex(index - size)
  }

  data class Cycle(val list: List<N>) {
    companion object {
      fun create(list: List<Int>): Cycle {
        return Cycle(list.mapIndexed { index, i -> N(index, i) })
      }
    }

    fun move(element: Int) : Cycle {
      val indexOfElement = list.indexOfFirst { it.index == element }
      val n = get(indexOfElement)

      if (n.value == 0) {
        println("0 does not move")
        return this
      }

      val steps = if (n.value < 0 ) n.value -1
      else if ((indexOfElement + n.value) > list.size) n.value +1
      else n.value

      val moveTo= list.newIndex(indexOfElement, steps)


      val next = copy(list = this.list.toMutableList().apply {
        removeAt(indexOfElement)
        add(moveTo, n)
      })

      println("${n.value} moves between ${next[moveTo-1].value} and ${next[moveTo+1].value}")

      return next
    }

    operator fun get(index: Int): N = list[list.effectiveIndex(index)]
  }

  fun input(test: Boolean) = AoCUtil.Input(year = 2022, day = 20, test = test)
    .nonEmptyLines.mapIndexed { i, e -> N(i, e.trim().toInt()) }.toMutableList().let {
      Cycle(it)
    }

}

fun main() {


  fun part1(test: Boolean): Int {
    var file = input(test)
    println("${file.list.map { it.value }}")

    (0 until file.list.size).forEach {
      file = file.move(it)
      println("${file.list.map { it.value }}")
    }

    val indexOfZero = file.list.indexOfFirst { it.value == 0 }

    return file[indexOfZero + 1000].value + file[indexOfZero + 2000].value + file[indexOfZero + 3000].value
  }

//  Mixing this file proceeds as follows:
//  1, 2, -3, 3, -2, 0, 4
//  1 moves between 2 and -3:
//  2, 1, -3, 3, -2, 0, 4
//  2 moves between -3 and 3:
//  1, -3, 2, 3, -2, 0, 4
//  -3 moves between -2 and 0:
//  1, 2, 3, -2, -3, 0, 4
//  3 moves between 0 and 4:
//  1, 2, -2, -3, 0, 3, 4
//  -2 moves between 4 and 1:
//  1, 2, -3, 0, 3, 4, -2
//  0 does not move:
//  1, 2, -3, 0, 3, 4, -2
//  4 moves between -3 and 0:
//  1, 2, -3, 4, 0, 3, -2
//  Then, the grove coordinates can be found by looking at the 1000th, 2000th, and 3000th numbers after the value 0, wrapping around the list as necessary. In the above example, the 1000th number after 0 is 4, the 2000th is -3, and the 3000th is 2; adding these together produces 3.
//
  println(part1(false))

}
