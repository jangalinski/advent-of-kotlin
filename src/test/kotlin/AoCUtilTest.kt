package io.github.jangalinski.aoc

import io.github.jangalinski.aoc.AoCUtil.factorsSequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AoCUtilTest {

  @Test
  fun `prime factors`() {
    val numbers = setOf(
      59, 65, 86, 56, 74, 57, 56, 63, 83, 50, 63, 56,
      93, 79, 74, 55,
      86, 61, 67, 88, 94, 69, 56, 91,
      76, 50, 51,
      77, 76,
      74,
      86, 85, 52, 86, 91, 95,
      79, 98,
      54, 65, 75, 74,
      79, 60, 97,
      74
    ).map { it.toLong() }
      .map { it * it }
      .toList().sorted()
    println(numbers)

    numbers.forEach {
      println("""
        $it  ${it.factorsSequence().toList()}
      """.trimIndent())
    }
  }

  @Test
  fun `correct long value`() {
    val p = AoCUtil.PrimeBase(mapOf(2 to 1))

    assertThat(p.longValue).isEqualTo(2L)
  }

  @Test
  fun `create empty`() {
    println(AoCUtil.PrimeBase())
  }
}
