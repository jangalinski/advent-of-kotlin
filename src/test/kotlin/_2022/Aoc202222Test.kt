package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AocTestHelper
import io.github.jangalinski.aoc._2022.Aoc202222.walk
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.Direction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource


internal class Aoc202222Test {
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  | y\x | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |   0 |   |   |   |   |   |   |   |   | O | O |  O |  X |    |    |    |    |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |   1 |   |   |   |   |   |   |   |   | O | X |  O |  O |    |    |    |    |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |   2 |   |   |   |   |   |   |   |   | X | O |  O |  O |    |    |    |    |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |   3 |   |   |   |   |   |   |   |   | O | O |  O |  O |    |    |    |    |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |   4 | O | O | O | X | O | O | O | O | O | O |  O |  X |    |    |    |    |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |   5 | O | O | O | O | L | O | O | O | X | O |  O |  O |    |    |    |    |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |   6 | O | O | X | O | O | O | O | X | O | O |  O |  O |    |    |    |    |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |   7 | O | O | O | O | O | O | O | O | O | O |  X |  O |    |    |    |    |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |   8 |   |   |   |   |   |   |   |   | O | O |  O |  X |  O |  O |  O |  O |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |   9 |   |   |   |   |   |   |   |   | O | O |  O |  O |  O |  X |  O |  O |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |  10 |   |   |   |   |   |   |   |   | O | X |  O |  O |  O |  O |  O |  O |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+
//  |  11 |   |   |   |   |   |   |   |   | O | O |  O |  O |  O |  O |  X |  O |
//  +-----+---+---+---+---+---+---+---+---+---+---+----+----+----+----+----+----+

  val krid = Aoc202222.input(true).first

  @ParameterizedTest
  @CsvSource(
    value = [
      "(4,5);UP;(4,4)",
      "(4,5);LEFT;(3,5)",
      "(4,5);RIGHT;(5,5)",
      "(4,5);DOWN;(4,6)",
      "(3,5);UP;(3,5)",
      "(15,11);RIGHT;(8,11)",
      "(14,8);UP;(14,8)"
    ], delimiterString = ";"
  )
  fun `test moves`(
    @ConvertWith(AocTestHelper.ToCell::class) start: Cell,
    direction: Direction,
    @ConvertWith(AocTestHelper.ToCell::class) expected: Cell
  ) {

    assertThat(krid.walk(start, direction)).isEqualTo(expected)
  }
}
