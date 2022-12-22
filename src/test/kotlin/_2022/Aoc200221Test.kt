package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc._2022.Aoc200221.Operator.PLUS
import io.github.jangalinski.aoc._2022.Aoc200221.Value
import io.github.jangalinski.aoc._2022.Aoc200221.Value.Expr
import io.github.jangalinski.aoc._2022.Aoc200221.Value.Num
import io.github.jangalinski.aoc._2022.Aoc200221.parse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


internal class Aoc200221Test {

  @ParameterizedTest
  @CsvSource(
    value = [
      "PLUS,2,3,5",
      "PLUS,a,3,a + 3",
      "MINUS,5,3,2",
      "TIMES,5,3,15",
      "DIVIDE,6,3,2",
      "PLUS,a,b, a + b",
      "TIMES,a,b, a * b",
      "DIVIDE,a,b, a / b",
      "MINUS,a,b, a - b",
    ]
  )
  fun calculate(
    operator: Aoc200221.Operator,
    first: String,
    second: String,
    expected: String
  ) {
    assertThat(operator(first, second)).isEqualTo(expected)
  }

  @Test
  fun `value transformation`() {
    assertThat(Pair("a", "5").parse()).isEqualTo(Pair("a", Num(5)))
    assertThat(Pair("a", "c + d").parse()).isEqualTo(Pair("a", Expr("c", "d", PLUS)))
    assertThat(Pair("a", "2 * 3").parse()).isEqualTo(Pair("a", Num(6)))
  }

  @Test
  fun `replace expr`() {
    var map: Map<String, Num> = mapOf("c" to Num(3), "d" to Num(5))
    var exp: Value = Expr("c", "d", PLUS).replace(map)

    println(exp.replace(map))


    map = mapOf("c" to Num(3))
    exp = Expr("c", "d", PLUS).replace(map)

    println(exp.replace(map))

  }
}
