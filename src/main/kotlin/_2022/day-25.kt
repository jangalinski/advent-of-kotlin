package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc._2022.Aoc202225.snafu
import java.math.BigInteger

object Aoc202225 {


  fun String.snafu(): BigInteger = this.reversed().foldIndexed(0.toBigInteger()) { index, sum, char ->
    val c = when (char) {
      '2' -> 2
      '1' -> 1
      '=' -> -2
      '-' -> -1
      else -> 0
    }.toBigInteger()

    val x = (5.toBigInteger().pow(index))*c


    sum + x
  }

  fun input(test: Boolean) = AoCUtil.Input(year = 2022, day = 25, test = test).nonEmptyLines

}

fun main() {

  println(Aoc202225.input(true).map { it.snafu() }.sumOf { it })

  //33010101016442

  //33010101016442

  println(33010101016442.toString(5))


}
