package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc._2022.Aoc2208.filterVisible
import io.github.jangalinski.aoc._2022.Aoc2208.toTreeView
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ArgumentConverter
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource

internal class Aoc2208Test {

  class ToIntList : ArgumentConverter {
    override fun convert(csv: Any, ctx: ParameterContext): List<Int> {
      require(csv is String)
      return csv.split(",").map(String::toInt)
    }

  }

  @Test
  fun `try`() {


    assertThat(listOf(3, 5, 3).filterVisible(3)).containsExactly(3, 5)
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "1,2,3; 4; 1,2,3",
      "4,2,3; 0; 4",
      "4,2,3; 4; 4",
      "3,3; 5; 3,3",
      "3,5,3; 5; 3,5",
      "3,5,3; 3; 3,5",
    ], delimiterString = ";"
  )
//  TreeView(cellValue=CellValue(x=3, y=2, value=3),
//  left=[3, 3],
//  right=[4, 9],
//  up=[3, 5, 3],
//  down=[3],
//  visibleLeft=[3],
//  visibleRight=[4],
//  visibleUp=[3],
//  visibleDown=[3],
  fun `filter visible  based on start`(
    @ConvertWith(ToIntList::class) input: List<Int>,
    value: Int,
    @ConvertWith(ToIntList::class) expected: List<Int>
  ) {
    assertThat(input.filterVisible(value)).containsExactly(*(expected.toTypedArray()))
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "1,2,3; 1,2,3",
      "4,2,3; 4",
      "3,3; 3,3",
      "3,5,3; 3,5",
      "3,5,3,6; 3,5,6",
    ], delimiterString = ";"
  )
  fun `filter visible`(
    @ConvertWith(ToIntList::class) input: List<Int>,
    @ConvertWith(ToIntList::class) expected: List<Int>
  ) {
    assertThat(input.filterVisible())
      .containsExactly(*(expected.toTypedArray()))
  }

  @Test
  fun `treeView scenic score - 1-2`() {
    val tv = Aoc2208.forest("_2022/08-1-test.txt")
      .toTreeView(1, 2)

    assertThat(tv.visibleUp).containsExactly(3)
    assertThat(tv.visibleLeft).containsExactly(5)
    assertThat(tv.visibleRight).containsExactly(1, 2)
    assertThat(tv.visibleDown).containsExactly(3, 5)
    assertThat(tv.scenicScore).isEqualTo(4)
  }

  @Test
  fun `treeView scenic score - 3-2`() {
    val tv = Aoc2208.forest("_2022/08-1-test.txt")
      .toTreeView(3, 2)
    assertThat(tv.visibleUp).containsExactly(3, 5)
    assertThat(tv.visibleLeft).containsExactly(3, 3)
    assertThat(tv.visibleRight).containsExactly(4, 9)
    assertThat(tv.visibleDown).containsExactly(3)
    assertThat(tv.scenicScore).isEqualTo(8)
  }
}
