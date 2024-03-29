package io.github.jangalinski.aoc

import com.github.freva.asciitable.AsciiTable
import io.github.jangalinski.aoc.AoCUtil.ListExt.peekPrint
import io.github.jangalinski.aoc.AoCUtil.ListExt.permutations
import io.github.jangalinski.aoc.AoCUtil.StringExt.chunkedByEmpty
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.Direction

object AoCUtil {

  object KridExt {

    fun Cell.adjacentInDir(vararg directions: Direction): List<Cell> = directions.map { this(it.singleStep) }

    fun <E : Any> Krid<E>.findByValue(value: E): Cell = this.iterator().asSequence()
      .filter { it.value == value }.single().cell

    fun <E : Any?> Krid<E>.table(value: (E) -> String = { "$it" }): String {

      val h = arrayOf("""y\x""") + (0 until width).map { "$it" }

      val d = rows().map { row ->
        arrayOf("${row.index}") + row.values.map(value)
      }.toTypedArray()



      return AsciiTable.getTable(h, d)
    }
  }

  object StringExt {
    fun String.nonEmptyLines() = lines().filterNot(String::isEmpty)

    fun String.chunkedByEmpty(): List<List<String>> = this.split("\n\n").map { it.nonEmptyLines() }

    fun String.toPair(splitter: String): Pair<String, String> {
      val (a, b) = this.split(splitter)
      return a.trim() to b.trim()
    }

    fun String.splitTrimmed(splitter: String): List<String> = this.split(splitter).map { it.trim() }.filterNot(String::isEmpty)

    fun String.intValues() = this.split("""\s+""".toRegex()).map { it.trim() }.filterNot { it.isBlank() }.map { it.toInt() }
    fun String.longValues() = this.split("""\s+""".toRegex()).map { it.trim() }.filterNot { it.isBlank() }.map { it.toLong() }
  }

  class Input(private val resource: String) {
    constructor(year: Int, day: Int, part: Int = 1, test: Boolean = false) : this(
      "_$year/${
        day.toString().padStart(2, '0')
      }-$part${if (test) "-test" else ""}.txt"
    )

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

    fun linesChunkedByEmpty() = contentTrimmed.chunkedByEmpty()
  }

  object ListExt {
    fun <T> List<T>.head(): Pair<T, List<T>> = this.toMutableList().let {
      val head = it.removeFirst()
      head to it.toList()
    }

    fun <T> List<T>.tail(): Pair<List<T>, T> = this.toMutableList().let {
      val tail = it.removeLast()
      it.toList() to tail
    }

    fun <T> Sequence<T>.peekPrint() = map {
      println(it)
      it
    }

    fun <T> Iterable<T>.peekPrint() = peek { println(it) }
    fun <T> Iterable<T>.peek(fn: (T) -> Unit) = map {
      fn(it)
      it
    }

    fun <T> List<T>.permutations(): Set<List<T>> {
      if (this.isEmpty()) return setOf(emptyList())

      val result: MutableSet<List<T>> = mutableSetOf()
      for (i in this.indices) {
        val list = this - this[i]
        list.permutations().forEach{
            item -> result.add(item + this[i])
        }
      }
      return result
    }
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

  fun factors(n:Int) {
    if (n < 1) return
    (1..n / 2)
      .filter { n % it == 0 }
      .forEach { print("$it ") }
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
  val l = listOf(0 to 1, 2 to 3, 3 to 4)
  val p = l.permutations()
  p.peekPrint()
}
