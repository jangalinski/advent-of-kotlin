package io.github.jangalinski.aoc22

import io.github.jangalinski.aoc22.RockPaperScissors.Result.*
import io.github.jangalinski.aoc22.RockPaperScissors.Symbol.*


fun main() {

  val abc = mapOf(
    "A" to ROCK,
    "B" to PAPER,
    "C" to SCISSORS
  )

  val input = Input("02-1.txt")

  // 14163
  fun silver(): Int {
    val xyz = mapOf(
      "X" to ROCK,
      "Y" to PAPER,
      "Z" to SCISSORS
    )

    val read = input.nonEmptyLines.map { line ->
      val (a, b) = line.split(" ")
      abc[a]!! to xyz[b]!!
    }

    return read.map {
      val result = it.second.against(it.first)
      result.points + it.second.points
    }.sum()
  }

  fun gold(): Int {
    val xyz = mapOf(
      "X" to LOSS,
      "Y" to DRAW,
      "Z" to WIN,
    )

    val read = input.nonEmptyLines.map { line ->
      val (a, b) = line.split(" ")
      abc[a]!! to xyz[b]!!
    }

    return read.sumOf { p ->
      val elfMove = p.first
      val shouldResult = p.second
      val myMove = elfMove.forResult(shouldResult)

      val result = myMove.against(elfMove)
      check(shouldResult == result) { "result must match" }
      val points = myMove.points + result.points

      println(listOf(elfMove, shouldResult, myMove, points))

      points
    }

  }


  println("02-silver: ${silver()}")
  println("02-gold:   ${gold()}")
}
