package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil.factorsSequence
import io.github.jangalinski.aoc.AoCUtil.toNonEmptyTrimmed
import io.github.jangalinski.aoc.AoCUtil.Input
import java.math.BigDecimal

object Aoc2211 {

  data class Item(val level: Long) {

    val factors by lazy {
      level.factorsSequence().toSet()
    }

    fun divisible(int: Int) = factors.contains(int.toLong())
  }

  sealed interface Operation : (Item) -> Item {

    data class Plus(val num: Long) : Operation {
      override fun invoke(item: Item): Item = item.copy(level = item.level + num)
    }

    data class Times(val num: Long) : Operation {
      override fun invoke(item: Item): Item = if (item.factors.contains(num)) item else item.copy(level = item.level * num)
    }

    object Square : Operation {
      override fun invoke(item: Item): Item = item
      override fun toString() = "Square"
    }
  }

  data class Test(val divisor: Int, val targetTrue: Int, val targetFalse: Int) : (Item) -> Int {
    override fun invoke(item: Item): Int = if (item.divisible(divisor)) targetTrue else targetFalse

  }

  data class PrimeMonkey(
    val id: Int,
    val items: ArrayDeque<Item>,
    val operation: Operation,
    val test: Test
  ) {
    companion object {

      fun parse(string: String): PrimeMonkey {
        var s = string.toNonEmptyTrimmed(";")
          .removePrefix("Monkey ")
          .replace("Starting items: ", "")
          .replace(" throw to monkey ", "")
          .replace("Operation: new = old ", "")
          .replace("Test: divisible by ", "")
          .replace("If ", "")
        val id = s.substringBefore(":").toInt()

        s = s.substringAfter(";")
        val items = s.substringBefore(";").split(",").map { it.trim().toLong() }.map { Item(it) }
        s = s.substringAfter(";")

        val (op, _val) = s.substringBefore(";").split(" ")
          .map { it.trim() }

        val operation = if (_val == "old") Operation.Square
        else if (op == "*") {
          Operation.Times(_val.toLong())
        } else {
          Operation.Plus(_val.toLong())
        }
        s = s.substringAfter(";")

        val (_d, _t, _f) = s.split(";")
        val bm = listOf(_t, _f).map { it.substringBefore(":").toBooleanStrict() to it.substringAfter(":").toInt() }.toMap()
        val test = Test(_d.toInt(), bm[true]!!, bm[false]!!)

        println(s)

        return PrimeMonkey(
          id = id,
          items = ArrayDeque(items),
          operation = operation,
          test = test
        )
      }

    }

    var count: Long = 0L

    fun throwItems(monkeys: List<PrimeMonkey>) {
      while (items.isNotEmpty()) {
        throwItem(monkeys, items.removeFirst())
      }
    }

    fun throwItem(monkeys: List<PrimeMonkey>, item: Item) {
      count += 1
      val nextItem = operation(item)
      val target = test(nextItem)

      monkeys[target].catchItem(nextItem)
    }

    fun catchItem(item: Item) {
      items.addLast(item)
    }
  }


  fun operationFn(op: String, value: String): (BigDecimal) -> BigDecimal {
    fun _self(): (BigDecimal) -> BigDecimal = { it }
    fun _num(n: Long): (BigDecimal) -> BigDecimal = { n.toBigDecimal() }
    val num: (BigDecimal) -> BigDecimal = if (value == "old") _self() else _num(value.toLong())

    return when (op) {
      "*" -> {
        { it.times(num(it)) }
      }

      else -> {
        { it.plus(num(it)) }
      }
    }
  }

  fun testFn(divisor: Int, onTrue: Int, onFalse: Int): (BigDecimal) -> Int = {
    val (_, rest) = it.divideAndRemainder(divisor.toBigDecimal())
    if (BigDecimal.ZERO == rest) onTrue else onFalse
  }

  fun parseOperation(s: String): (BigDecimal) -> BigDecimal {
    val (_op, _val) = s.removePrefix("Operation: new = old ").split(" ").map { it.trim() }
    return operationFn(_op, _val)
  }

  fun parseTest(lines: List<String>): (BigDecimal) -> Int {
    require(lines.size == 3) { "was: $lines" }
    val (_div, _true, _false) = lines
    val d = _div.removePrefix("Test: divisible by ").trim().toInt()

    fun extract(s: String): Pair<Boolean, Int> {
      val (_b, _t) = s.removePrefix("If ").replace(": throw to monkey ", ",").split(",")

      return _b.toBooleanStrict() to _t.trim().toInt()
    }

    val m = listOf(_true, _false).map { extract(it) }.toMap()

    return testFn(d, m[true]!!, m[false]!!)
  }

  fun parse(input: String, divide: Boolean = true): Monkey {
    val lines = ArrayDeque(input.lines().map { it.trim() })

    val id = lines.removeFirst().removePrefix("Monkey ").removeSuffix(":").trim().toInt()
    val items = lines.removeFirst().removePrefix("Starting items: ")
      .split(",")
      .map { it.trim().toBigDecimal() }
    val operation = parseOperation(lines.removeFirst())
    val test = parseTest(lines.filterNot { it.isEmpty() })

    return Monkey(
      id = id,
      items = ArrayDeque(items),
      operation = operation,
      test = test,
      divide = divide
    )
  }


  fun read(file: String, divide: Boolean = true): List<Monkey> = Input(file).contentTrimmed.split("\n\n")
    .map { parse(it, divide) }


  data class Monkey(
    val id: Int,
    val items: ArrayDeque<BigDecimal> = ArrayDeque(),
    val operation: (BigDecimal) -> BigDecimal,
    val test: (BigDecimal) -> Int,
    val divide: Boolean = true
  ) {

    var count = 0L

    fun throwItems(monkeys: List<Monkey>): Monkey {
      while (items.isNotEmpty()) {
        throwItem(monkeys)
      }
      return this
    }

    fun throwItem(monkeys: List<Monkey>): Monkey {
      require(items.isNotEmpty())

      val newItem = process(items.removeFirst())
      val target = test(newItem)

      monkeys[target].catch(newItem)

      return this
    }

    fun catch(item: BigDecimal) = items.addLast(item)

    fun process(item: BigDecimal): BigDecimal {
      count += 1
      val new = operation(item)

      return if (divide) {
        val (n, _) = new.divideAndRemainder(3.toBigDecimal())
        return n
      } else {
        new
      }
    }

    override fun toString(): String {
      return "Monkey(id=$id, items=$items, count=$count)"
    }
  }
}

fun main() {

  fun part1(): Long {
    val monkeys = Aoc2211.read("_2022/11-1-test.txt")

    println(monkeys)
    repeat(20) { _ ->
      monkeys.forEach { m -> m.throwItems(monkeys) }
    }
    println(monkeys)

    return monkeys.map { it.count }.sortedDescending().take(2).reduce { n, c -> n * c }
  }

  fun part2(): Long {
    val monkeys = Input("_2022/11-1-test.txt").contentTrimmed.split("\n\n")
      .map { Aoc2211.PrimeMonkey.parse(it) }


    repeat(1_000) { _ ->
      println(".")
      monkeys.forEach { m -> m.throwItems(monkeys) }
    }


//    repeat(1_000) { _ ->
//      print(".")
//      monkeys.forEach { m -> m.throwItems(monkeys) }
//    }
//    println(monkeys)
//
    return monkeys.map { it.count }.sortedDescending().take(2).reduce { n, c -> n * c }
  }

  println("PART 1: ${part1()}")
  println("PART 2: ${part2()}")
}
