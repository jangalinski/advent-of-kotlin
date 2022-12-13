package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc._2022.Aoc202013.Item.Single
import io.github.jangalinski.aoc._2022.Aoc202013.parse
import java.util.*

object Aoc202013 {

  fun String.tokenize(removeOuter: Boolean = true): List<Token> = this.replace(" ", "").replace("10", "A")
    .mapNotNull {
      when (it) {
        '[' -> Token.Open
        ']' -> Token.Close
        ',' -> null
        'A' -> Token.Digit(10)
        else -> Token.Digit(it.digitToInt())
      }
    }

  sealed class Token(private val toString: String) {
    object Open : Token("(")

    object Close : Token(")")

    data class Digit(val value: Int) : Token("$value")

    override fun toString() = toString
  }


  sealed class Item : Comparable<Item> {
    abstract operator fun plus(item: Item): Multi

    abstract fun multi(): Multi

    abstract fun head(): Pair<Item, Item>

    /**
     * Compares this object with the specified object for order.
     * Returns a
     *
     * * negative integer - this object is less than the specified object.
     * * zero - this object is  equal to the specified object.
     * * a positive integer - this object is greater than the specified object.
     */
    override fun compareTo(other: Item): Int = when (this) {
      is Multi -> when (other) {
        is Single -> this.compareTo(other.multi())
        Empty -> 1
        is Multi -> {
          val (ti, tt) = this.head()
          val (oi, ot) = other.head()

          val c = ti.compareTo(oi)
          if (c == 0) {
            tt.compareTo(ot)
          } else {
            c
          }
        }
      }

      is Single -> when (other) {
        is Multi -> this.multi().compareTo(other)
        is Single -> this.value.compareTo(other.value)
        Empty -> 1
      }

      Empty -> when (other) {
        is Multi -> -1
        is Single -> -1
        Empty -> 0
      }
    }

    data class Single(val value: Int) : Item() {
      override operator fun plus(item: Item): Multi = multi() + item
      override fun multi(): Multi = Multi(listOf(this))
      override fun head(): Pair<Item, Item> = this to Empty

      override fun toString() = "$value"
    }

    data class Multi(val values: List<Item> = emptyList()) : Item(), List<Item> by values {
      override operator fun plus(item: Item): Multi = Multi(values + item)
      override fun multi(): Multi = this
      override fun head(): Pair<Item, Item> = if (values.isEmpty()) {
        Empty.head()
      } else {
        this.values.toMutableList().let {
          it.removeFirst() to Multi(it)
        }
      }

      override fun toString() = "$values"
    }

    object Empty : Item() {
      override fun plus(item: Item): Multi = multi() + item
      override fun multi(): Multi = Multi()
      override fun head(): Pair<Item, Item> = Empty to Empty
    }

    override fun toString() = "${emptyList<Item>()}"
  }

  fun String.parse(): Item.Multi = tokenize().fold(Stack<Item>()) { c, n ->
    when (n) {
      Token.Open -> c.push(Item.Empty)
      Token.Close -> {
        if (c.size > 1) {
          val inner = c.pop()
          c.push(c.pop() + inner)
        }
      }

      is Token.Digit -> {
        val multi = c.pop() + Single(n.value)
        c.push(multi)
      }
    }
    c
  }.pop().multi()

}


fun main() {
  val input = AoCUtil.Input(year = 2022, day = 13, test = false).linesChunkedByEmpty()
    .map { lines ->
      val (a, b) = lines.map { it.parse() }
      a to b
    }

  fun part1(): Int = input.mapIndexed { index, pair -> index to (pair.first < pair.second) }.filter { it.second }.sumOf { it.first + 1 }

  fun part2() : Int {
    val m2 = "[[2]]".parse()
    val m6 = "[[6]]".parse()

    val items = input.fold(Aoc202013.Item.Empty + m2 + m6) { c,n ->
      c + n.first + n.second
    }.sorted()

    return (items.indexOf(m2) +1) * (items.indexOf(m6) + 1)
  }

  println("PART1: ${part1()}")
  println("PART2: ${part2()}")
}
