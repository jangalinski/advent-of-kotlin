package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc._2022.Aoc202220.Cycle
import io.github.jangalinski.aoc._2022.Aoc202220.N
import io.github.jangalinski.aoc._2022.Aoc202220.effectiveIndex
import io.github.jangalinski.aoc._2022.Aoc202220.newIndex
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class Aoc202220Test {

  @ParameterizedTest
  @CsvSource(
    value = [
      "0,1,1",
      "0,2,2",
      "0,-1,9",
      "9,1,0",
      "0,31,1",
      "0,-31,9",
    ]
  )
  fun `loop through list`(old: Int, move: Int, expected: Int) {
    require(old in (0 until 10))
    require(expected in (0 until 10))
    val list = List(10) { if (it == old) 1 else 0 }

    assertThat(list.newIndex(old, move)).isEqualTo(expected)
  }

  @ParameterizedTest
  @CsvSource(value = [
    "0,0",
    "1,1",
    "10,0",
    "21,1",
    "-2,8",
    "-11,9",
    "-10,0",
  ])
  fun `get at`(index: Int, expected: Int) {
    val cycle = Cycle.create(List(10) { it })

    // 0123456789
    assertThat(cycle[index]).isEqualTo(N(expected, expected))
  }

  @ParameterizedTest
  @CsvSource(value = [
    "0,0",
    "9,9",
    "10,0",
    "31,1",
    "-1,9",
    "-11,9",
  ])
  fun `effective index`(index: Int, expected: Int) {
    val list = List(10) {it}

    assertThat(list.effectiveIndex(index)).isEqualTo(expected)
  }
}
