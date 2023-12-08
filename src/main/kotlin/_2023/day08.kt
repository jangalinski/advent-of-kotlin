package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.ListExt.head
import io.github.jangalinski.aoc.AoCUtil.StringExt.toPair

fun main() {

//  val input = AoCUtil.Input(year = 2023, day= 8, part = 3, test = true)
  val input = AoCUtil.Input(year = 2023, day= 8, part = 1, test = false)

  fun parse(line:String) : Pair<String,Pair<String,String>>  {
    return line.substring(0,3) to (line.substring(7,10) to line.substring(12,15))
  }

  fun parse(input: AoCUtil.Input): Pair<String, Map<String, Map<Char, String>>> {
    val (path, map) = input.linesChunkedByEmpty().head()

    return path.single() to map.single().map{parse(it)}.associate {
      it.first to mapOf('L' to it.second.first, 'R' to it.second.second)
    }
  }

  fun endlessSequence(s:String): Sequence<Char> = sequence {
    var i = 0
    while (true) {
      yield(s[i])
      if (i == s.lastIndex) {
        i = 0
      } else
        i += 1
    }
  }

  val parsed = parse(input)
  val sequence = endlessSequence(parsed.first)
  val map = parsed.second


//  var node = "AAA"
//  var count = 0
//  val s = sequence.takeWhile { node != "ZZZ" }.map {
//    node = map[node]!![it.first]!!
//    it
//  }.last()
//
  var nodes = map.keys.filter { it.endsWith("A") }
  var count = 0L
  val ends = mutableListOf<Long>()
  var print = 0
  println(nodes)
  val s = sequence.takeWhile { nodes.isNotEmpty() }.map {p->
    val next = nodes.map {  map[it]!![p]!! }
    val end = next.filter { it.endsWith('Z') }
    nodes = next.filterNot { end.contains(it) }


    if (print % 100_000_000 == 0) {
      println(nodes)
      print = 0
    }
    count += 1
    print += 1

    end.forEach { ends.add(count) }

    p
  }.last()

  println(ends)

  // 11283670395017

  ends.map { it.toInt() }.forEach { AoCUtil.printFactors(it) }

  """
    43
    47
    59
    61
    73
    79
  """.trimIndent().lines().map { it.trim() }.filterNot { it.isBlank() }.map { it.toPair(" ").let { it.first.toLong() * it.second.toLong() } }
    .fold(1L){a,c -> a*c}
    .also { println(it) }
}
