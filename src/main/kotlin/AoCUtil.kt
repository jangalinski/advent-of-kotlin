package io.github.jangalinski.aoc

import com.github.freva.asciitable.AsciiTable
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.model.Cell

object AoCUtil {

  object KridExt {

    fun <E:Any> Krid<E>.findByValue(value: E) : Cell = this.iterator().asSequence()
      .filter { it.value == value }.single().cell

    fun <E:Any> Krid<E>.table() : String {

      val h = arrayOf("""y\x""") + (0 until width).map { "$it" }

      val d = rows().map { row ->
        arrayOf("${row.index}") + row.values.map { "$it" }
      }.toTypedArray()



      return AsciiTable.getTable(h,d)
    }
  }

  class Input(private val resource: String) {
    companion object {
      operator fun invoke(year: Int, day: Int, part: Int = 1, test: Boolean = false): Input {
        return Input("_$year/${day.toString().padStart(2, '0')}-$part${if (test) "-test" else ""}.txt")
      }

      fun String.nonEmptyLines() = lines().filterNot(String::isEmpty)

      fun String.chunkedByEmpty(): List<List<String>> = this.split("\n\n").map { it.nonEmptyLines() }
    }


    val contentRaw by lazy {
      requireNotNull(Input::class.java.getResource("/$resource")).readText()
    }

    val contentTrimmed = contentRaw.lines().joinToString("\n") { it.trim() }

    val nonEmptyLines by lazy {
      contentTrimmed.lines().filterNot { it.isEmpty() }
    }

    fun digitKrid() = Krids.krid(string = contentRaw, emptyElement = 0, parse = { it.digitToInt() })

    fun <E : Any> krid(emptyElement: E, parse: (Char) -> E): Krid<E> =
      Krids.krid(string = contentRaw, emptyElement = emptyElement, parse = parse)

    fun krid() = Krids.krid(contentRaw)

    override fun toString() = "Input(file='$resource', contentRaw='$contentRaw')"

  }


  fun String.toNonEmptyTrimmedLines() = this.lines()
    .map { it.trim() }
    .filterNot { it.isEmpty() }

  fun String.toNonEmptyTrimmed(separator: String = "\n") = toNonEmptyTrimmedLines().joinToString(separator = separator)


  fun Long.factorsSequence(): Sequence<Long> {
    val n = this
    return sequence {
      (1..n / 2).forEach {
        if (n % it == 0L) yield(it)
      }
      yield(n)
    }
  }

  data class PrimeBase(
    val map: Map<Int, Int>
  ) {
    companion object {

      operator fun invoke(): PrimeBase {
        return PrimeBase(
          mapOf(
            1 to 0,
            2 to 0,
            3 to 0,
            5 to 0,
            7 to 0,
            11 to 0,
            13 to 0,
            17 to 0,
            19 to 0,
            23 to 0,
            29 to 0,
            31 to 0,
            37 to 0,
            41 to 0,
            43 to 0,
            47 to 0,
            53 to 0,
            59 to 0,
            61 to 0,
            67 to 0,
            71 to 0,
            73 to 0,
            79 to 0,
            83 to 0,
            89 to 0,
            97 to 0,
          )
        )
      }

      operator fun invoke(num: Int): PrimeBase {
        return PrimeBase(mapOf(1 to 0))
      }
    }

    val longValue by lazy {
      map.entries.fold(0L) { s, (k, v) ->
        s + k * v
      }
    }
  }

  fun printFactors(n: Int) {
    if (n < 1) return
    print("$n => ")
    (1..n / 2)
      .filter { n % it == 0 }
      .forEach { print("$it ") }
    println(n)
  }


}

fun main(args: Array<String>) {
  val numbers = intArrayOf(11, 21, 32, 45, 67, 96)
  for (number in numbers) AoCUtil.printFactors(number)
}
