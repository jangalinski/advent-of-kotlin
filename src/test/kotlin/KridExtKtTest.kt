package io.github.jangalinski.aoc

import io.toolisticon.lib.krid.model.step.Direction
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class KridExtKtTest {

  @ParameterizedTest
  @CsvSource(
    value = [
      "UP,LEFT,UP_LEFT",
      "UP,UP,",
    ]
  )
  fun `combine directions`(a: Direction, b: Direction, expected: Direction?) {
    if (expected != null) {
      assertThat(a combine b).isEqualTo(expected)
    } else {
      assertThatThrownBy { a combine b }
        .isInstanceOf(IllegalArgumentException::class.java)
    }
  }
}
