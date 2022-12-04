package io.github.jangalinski.aoc22


class Input(resource: String) {

  val content = requireNotNull(Input::class.java.getResource("/$resource")).readText().lines().joinToString("\n") { it.trim() }

  val nonEmptyLines by lazy {
    content.lines().filterNot { it.isEmpty() }
  }
}

object RockPaperScissors {

  enum class Symbol(val against: (Symbol) -> Result, val forResult: (Result) -> Symbol) {
    ROCK(against = {
      when (it) {
        ROCK -> Result.DRAW
        PAPER -> Result.LOSS
        SCISSORS -> Result.WIN
      }
    },
      forResult = {
        when (it) {
          Result.WIN -> PAPER
          Result.DRAW -> ROCK
          Result.LOSS -> SCISSORS
        }
      }),
    PAPER(against = {
      when (it) {
        ROCK -> Result.WIN
        PAPER -> Result.DRAW
        SCISSORS -> Result.LOSS
      }
    },
      forResult = {
        when (it) {
          Result.WIN -> SCISSORS
          Result.DRAW -> PAPER
          Result.LOSS -> ROCK
        }

      }),
    SCISSORS(against = {
      when (it) {
        ROCK -> Result.LOSS
        PAPER -> Result.WIN
        SCISSORS -> Result.DRAW
      }
    },
      forResult = {
        when (it) {
          Result.WIN -> ROCK
          Result.DRAW -> SCISSORS
          Result.LOSS -> PAPER
        }
      });

    val points = ordinal + 1
  }

  enum class Result {
    LOSS,
    DRAW,
    WIN;

    val points = ordinal * 3
  }
}
