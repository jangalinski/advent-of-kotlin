package io.github.jangalinski.aoc._2022

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


class RockPaperScissorsTest {
//
//  @ParameterizedTest
//  @CsvSource(value = [
//    "ROCK, ROCK, DRAW",
//    "ROCK, PAPER, LOSS",
//    "ROCK, SCISSORS, WIN",
//
//    "PAPER, ROCK, WIN",
//    "PAPER, PAPER, DRAW",
//    "PAPER, SCISSORS, LOSS",
//
//    "SCISSORS, ROCK, LOSS",
//    "SCISSORS, PAPER, WIN",
//    "SCISSORS, SCISSORS, DRAW",
//  ])
//  fun playAgainst(a: Symbol, b: Symbol, expected: Result) {
//    assertThat(a.against(b)).isEqualTo(expected)
//  }
// @ParameterizedTest
//  @CsvSource(value = [
//    "ROCK, WIN, PAPER",
//    "ROCK, DRAW, ROCK",
//    "ROCK, LOSS, SCISSORS",
//
//    "PAPER, WIN, SCISSORS",
//    "PAPER, DRAW, PAPER",
//    "PAPER, LOSS, ROCK",
//
//    "SCISSORS, WIN, ROCK",
//    "SCISSORS, DRAW, SCISSORS",
//    "SCISSORS, LOSS, PAPER",
//  ])
//  fun playForResult(symbol: Symbol, result: Result, expected: Symbol) {
//    assertThat(symbol.forResult(result)).isEqualTo(expected)
//  }
//
//  @Test
//  fun `points for symbol`() {
//    assertThat(ROCK.points).isEqualTo(1)
//    assertThat(PAPER.points).isEqualTo(2)
//    assertThat(SCISSORS.points).isEqualTo(3)
//  }
//  @Test
//  fun `points for result`() {
//    assertThat(WIN.points).isEqualTo(6)
//    assertThat(DRAW.points).isEqualTo(3)
//    assertThat(LOSS.points).isEqualTo(0)
//  }
}
