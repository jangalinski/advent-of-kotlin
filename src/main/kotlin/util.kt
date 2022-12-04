package io.github.jangalinski.aoc22


class Input(resource: String) {

  val content = requireNotNull(Input::class.java.getResource("/$resource")).readText().lines().joinToString("\n") { it.trim() }

  val nonEmptyLines by lazy {
    content.lines().filterNot { it.isEmpty() }
  }
}

