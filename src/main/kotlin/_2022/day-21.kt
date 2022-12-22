package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil.Input
import io.github.jangalinski.aoc.AoCUtil.StringExt.toPair
import io.github.jangalinski.aoc._2022.Aoc200221.Value.Num
import io.github.jangalinski.aoc._2022.Aoc200221.input
import io.github.jangalinski.aoc._2022.Aoc200221.partitionNumbers
import java.math.BigInteger

object Aoc200221 {
  fun String.triple(): Triple<String, String, String> {
    val (a, o, b) = this.split(" ")
    return Triple(a, o, b)
  }

  enum class Operator(val sign: String, val expr: (BigInteger, BigInteger) -> BigInteger) : (String, String) -> String {
    PLUS(sign = "+", expr = { a, b -> a + b }),
    TIMES(sign = "*", expr = { a, b -> a * b }),
    DIVIDE(sign = "/", expr = { a, b -> a / b }),
    MINUS(sign = "-", expr = { a, b -> a - b }),
    EQUAL(sign = "=", expr = { a, b -> a.compareTo(b).toBigInteger() })
    ;

    companion object {
      private val bySign = values().associateBy { it.sign }

      fun valueOfSign(sign: String): Operator = requireNotNull(bySign[sign])
    }

    override fun invoke(first: String, second: String): String {
      val i1 = first.toBigIntegerOrNull()
      val i2 = second.toBigIntegerOrNull()

      return if (i1 != null && i2 != null) {
        "${expr(i1, i2)}"
      } else {
        "$first $sign $second"
      }
    }
  }

  fun Pair<String, String>.parse(): Pair<String, Value> = if (second.toIntOrNull() != null) {
    first to Num(second.toBigInteger())
  } else if (second == "?") {
    first to Value.Human
  } else {
    val t = second.triple()
    val o = Operator.valueOfSign(t.second)
    first to Value.Expr(t.first, t.third, o)()
  }

  sealed interface Value : () -> Value {

    fun replace(map: Map<String, Num>): Value
    data class Num(val n: BigInteger) : Value {
      constructor(n: Int) : this(n.toBigInteger())

      override fun replace(map: Map<String, Num>): Value = this

      override fun invoke(): Value = this
      override fun toString(): String = n.toString()


    }

    data class Expr(val first: String, val second: String, val operator: Operator) : Value, () -> Value {
      private val expr = "$first ${operator.sign} $second"
      override fun invoke(): Value {
        val calc = operator(first, second)
        return if (expr == calc) this else Num(calc.toBigInteger())
      }

      override fun replace(map: Map<String, Num>): Value = copy(
        first = map[first]?.n?.toString() ?: first,
        second = map[second]?.n?.toString() ?: second
      )()

      override fun toString(): String = expr


    }

    object Human : Value {
      override fun replace(map: Map<String, Num>): Value = this

      override fun invoke(): Value = this

      override fun toString() = "?"
    }

  }

  fun List<Pair<String, Value>>.partitionNumbers() = this.partition { it.second is Num }


  fun input(test: Boolean, part: Int): List<Pair<String, Value>> = Input(year = 2022, day = 21, test = test, part = part).nonEmptyLines
    .map {
      it.toPair(": ").parse()
    }
}

fun main() {
  fun part1(test: Boolean): BigInteger {
    val input = input(test, 1)

    var partition = input.partitionNumbers()

    while (partition.second.any { !(it.second is Num) }) {
      val map = partition.first.map { it.first to it.second as Num }.toMap()
      val next = partition.second.map { it.first to it.second.replace(map) }

      partition = next.partitionNumbers()
    }

    return partition.first.filter { it.second is Num }.map { it.second as Num }.map { it.n }.first()
  }


  fun part2(test: Boolean):BigInteger {
    val input = input(test, 2)

    var partition = input.partitionNumbers()

    var last: List<Pair<String, Aoc200221.Value>> = partition.second

    while (true) {
      val map = partition.first.map { it.first to it.second as Num }.toMap()

      val next = partition.second.map { it.first to it.second.replace(map) }

      last = partition.second
      partition = next.partitionNumbers()

      partition.first.forEach(::println)
      println("-")
      partition.second.forEach(::println)
      println("--------- ......")

    }

    input.forEach(::println)


    return BigInteger.ONE
  }

  //check(BigInteger.valueOf(78342931359552L) == part1(false))

  part2(true)
}
