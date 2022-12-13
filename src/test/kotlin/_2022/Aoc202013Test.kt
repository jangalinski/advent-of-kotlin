package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc._2022.Aoc202013.Item.Single
import io.github.jangalinski.aoc._2022.Aoc202013.parse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


internal class Aoc202013Test {

  @Test
  fun `list of ints`() {
    val list = "[1,1,3,1,1]".parse()

    assertThat(list.values).hasSize(5)
    assertThat(list[0]).isEqualTo(Single(1))
    assertThat(list[1]).isEqualTo(Single(1))
    assertThat(list[2]).isEqualTo(Single(3))
    assertThat(list[3]).isEqualTo(Single(1))
    assertThat(list[4]).isEqualTo(Single(1))
  }

  @Test
  fun `parse (1,(2,(3,(4,(5,6,7)))),8,9)`() {
    val items = "[1,[2,[3,[4,[5,6,7]]]],8,9]".parse()

    assertThat(items.values).hasSize(4)
  }

  @Test
  fun `parse 12`() {
    val list = "[10]".parse()

    assertThat(list.values).hasSize(1)
    assertThat(list[0]).isInstanceOf(Single::class.java)
    assertThat(list[0]).isEqualTo(Single(10))
  }

  @Test
  fun `compare two ints`() {
    assertThat(Single(1)).isLessThan(Single(2))
  }
}
