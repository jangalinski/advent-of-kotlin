package io.github.jangalinski.aoc._2022

import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.Direction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ArgumentConverter
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource


internal class Aoc2209Test {

  class ToCell : ArgumentConverter {
    override fun convert(input: Any, p1: ParameterContext): Cell {
      require(input is String)

      val (x,y) = input.split(":").map(String::toInt)
      return Cell(x,y)
    }
  }


  @ParameterizedTest
  @CsvSource(value = [
    "0:0,0:0,NONE",
    "1:0,0:0,NONE",
    "0:1,0:0,NONE",
    "-1:0,0:0,NONE",
    "-1:-1,0:0,NONE",
    "2:0,0:0,RIGHT",
    "-2:0,0:0,LEFT",
    "-2:-1,0:0,UP_LEFT",
    "0:-2,0:0,UP",
    "0:2,0:0,DOWN",
    "2:-4,4:-3,UP_LEFT",
  ])
  fun `find tail move`(@ConvertWith(ToCell::class) h:Cell, @ConvertWith(ToCell::class) t:Cell, expected: Direction) {
    assertThat(Aoc2209.findTMove(h,t)).isEqualTo(expected)
  }
}
