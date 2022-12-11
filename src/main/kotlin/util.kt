package io.github.jangalinski.aoc

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids


class Input(resource: String) {
  companion object {
    fun String.nonEmptyLines() = lines().filterNot(String::isEmpty)

    fun String.chunkedByEmpty(): List<List<String>> =  this.split("\n\n").map { it.nonEmptyLines() }
  }
  val contentRaw = requireNotNull(Input::class.java.getResource("/$resource")).readText()

  val contentTrimmed = contentRaw.lines().joinToString("\n") { it.trim() }

  val nonEmptyLines by lazy {
    contentTrimmed.lines().filterNot { it.isEmpty() }
  }

  fun digitKrid() = Krids.krid(string = contentRaw, emptyElement = 0, parse = { it.digitToInt() })

  fun <E:Any> krid(emptyElement: E, parse: (Char)->E): Krid<E> = Krids.krid(string = contentRaw, emptyElement = emptyElement, parse = parse)

}

