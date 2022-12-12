package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc._2022.AoC202212.HeightMap
import io.github.jangalinski.aoc._2022.AoC202212.canMoveTo
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


internal class AoC202212Test {

  private fun String.cell() = cell(substringBefore(".").toInt(), substringAfter(".").toInt())

  private val heightMap = HeightMap(
    krid = krid(
      """
    Sabqponm
    abcryxxl
    accszExk
    acctuvwj
    abdefghi
  """.trimIndent()
    )
  )

  @ParameterizedTest
  @CsvSource(
    value = [
      "0.0;1.0,0.1",
      "5.2;",
      "4.3;5.3,4.4,3.3",
      "5.3;5.2,6.3,5.4,4.3",
    ], delimiterString = ";"
  )
  fun `possible next`(c: String, expectedNext: String?) {
    val cell = c.cell()
    val expected = expectedNext?.split(",")?.map { it.trim().cell() } ?: emptyList()

    assertThat(heightMap.next(cell)).containsExactlyInAnyOrderElementsOf(expected)
  }

  @ParameterizedTest
  @CsvSource(value = [
    "S,a,true",
    "S,z,true",
    "E,a,false",
    "E,z,false",
    "a,E,false",
    "z,E,true",
    "S,E,true",
    "a,b,true",
    "a,c,false",
    "c,d,true",
    "c,e,false",
    "c,b,true",
    "c,a,true",
    "c,S,false",
  ])
  fun `can move to`(s:Char,t:Char,expected:Boolean) {
    assertThat(s canMoveTo t).isEqualTo(expected)
  }
}
