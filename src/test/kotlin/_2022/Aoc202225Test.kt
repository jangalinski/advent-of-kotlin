package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc._2022.Aoc202225.snafu
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class Aoc202225Test {

  @ParameterizedTest
  @CsvSource(
    value = [
      "1=-0-2,1747",
      "12111,906",
      "2=0=,198",
      "21,11",
      "2=01,201",
      "111,31",
      "20012,1257",
      "112,32",
      "1=-1=,353",
      "1-12,107",
      "12,7",
      "1=,3",
      "122,37",
    ]
  )
  fun `snafu to dec`(snafu: String, expected: Int) {
    assertThat(snafu.snafu()).isEqualTo(expected)
  }
}
