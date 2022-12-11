package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc._2022.Aoc2211.Monkey
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


internal class Aoc2211Test {

  @ParameterizedTest
  @CsvSource(
    value = [
      "Operation: new = old * 19, 1, 19",
      "Operation: new = old * 8, 2, 16",
      "Operation: new = old + 8, 2, 10",
      "Operation: new = old + old, 2, 4",
      "Operation: new = old * old, 3, 9",
    ]
  )
  fun `parse operation`(s: String, old: Long, new: Long) {
    val op = Aoc2211.parseOperation(s)
    assertThat(op(old.toBigDecimal())).isEqualTo(new.toBigDecimal())
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "Test: divisible by 13#If true: throw to monkey 3#If false: throw to monkey 0, 13, 3",
      "Test: divisible by 13#If true: throw to monkey 3#If false: throw to monkey 0, 14, 0",
    ]
  )
  fun `parse test`(s: String, old: Long, new: Long) {
    val test = Aoc2211.parseTest(s.split("#"))
    assertThat(test(old.toBigDecimal())).isEqualTo(new)
  }

  @Test
  fun `process item`() {
    val m = Monkey(0, ArrayDeque(listOf(17.toBigDecimal())), Aoc2211.operationFn("*", "2"), Aoc2211.testFn(17, 1, 2))
    assertThat(m.process(17.toBigDecimal()))
      .isEqualTo(11.toBigDecimal())

    assertThat(m.process(11.toBigDecimal()))
      .isEqualTo(7.toBigDecimal())
  }

  @Test
  fun `parse 0`() {
    val m = Aoc2211.parse(
      """
      Monkey 0:
      Starting items: 79, 98
      Operation: new = old * 19
      Test: divisible by 23
      If true: throw to monkey 2
      If false: throw to monkey 3
    """.trimIndent()
    )

    assertThat(m.id).isEqualTo(0)
    assertThat(m.items).containsExactly(79.toBigDecimal(), 98.toBigDecimal())
    assertThat(m.operation(2.toBigDecimal())).isEqualTo(38.toBigDecimal())
    assertThat(m.test(23.toBigDecimal())).isEqualTo(2)
    assertThat(m.test(24.toBigDecimal())).isEqualTo(3)

  }

  @Test
  fun `parse 2`() {
    val m = Aoc2211.parse(
      """
      Monkey 2:
      Starting items: 79, 60, 97
      Operation: new = old * old
      Test: divisible by 13
      If true: throw to monkey 1
      If false: throw to monkey 3
      """.trimIndent()
    )

    assertThat(m.id).isEqualTo(2)
    assertThat(m.items).containsExactly(79.toBigDecimal(), 60.toBigDecimal(), 97.toBigDecimal())
    assertThat(m.operation(9.toBigDecimal())).isEqualTo(81.toBigDecimal())
    assertThat(m.test(13.toBigDecimal())).isEqualTo(1)
    assertThat(m.test(14.toBigDecimal())).isEqualTo(3)

  }

}
