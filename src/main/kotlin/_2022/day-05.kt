package io.github.jangalinski.aoc._2022
import io.github.jangalinski.aoc.AoCUtil.Input
import io.github.jangalinski.aoc.AoCUtil.StringExt.chunkedByEmpty
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids


fun main() {
  data class Move(val num:Int, val from:Int, val to: Int) {

    fun move(qs: List<ArrayDeque<Char>>): List<ArrayDeque<Char>> {
      repeat(num) {
        val taken = qs[from].removeFirst()
        qs[to].addFirst(taken)
      }
      return qs
    }

    fun move2(qs: List<ArrayDeque<Char>>): List<ArrayDeque<Char>> {
      val taken = mutableListOf<Char>()
      repeat(num) {
        val c = qs[from].removeFirst()
        taken.add(c)
      }
      taken.reversed().forEach {
        qs[to].addFirst(it)
      }
      return qs
    }
  }

  val x = Input("05-1.txt").contentRaw.chunkedByEmpty().let{ input ->
    val a = input[0].dropLast(1)
    val maxLength = a.maxBy { it.length }.length
    val a1 = a.map { it.padEnd(maxLength, ' ') }
      .map { it.replace("    ", "[ ] ") }
      .map { it.replace("] [", "][") }
      .map { it.replace("]  [", "][") }
      .map { it.trim() }
      .map { it.replace("][","") }
      .map { it.replace("]","") }
      .map { it.replace("[","") }
      .map { it.replace(" ",".") }

    val moves = input[1].map {
      it.removePrefix("move ")
        .replace(" from ",",")
        .replace(" to ", ",")
    }.map {
      val (m,f,t) = it.split(",").map { it.toInt() }
      Move(m,  f-1, t-1)
    }


    val k: Krid<Char?> = Krids.krid(string = a1.joinToString("\n"), emptyElement = null, parse = { if (it == '.') null else it })

    k.columns().map { it.filterNotNull() }.map { ArrayDeque(it) } to moves
  }

  //val q1 = x.second.fold(x.first) { qs, m -> m.move(qs)}
  val q2 = x.second.fold(x.first) { qs, m -> m.move2(qs)}

  // 1: WCZTHTMPS
  // 2: BLSGJSDTS
  println(q2.map { it.first() }.joinToString())

//  fun String.nonEmptyLines() = lines().filterNot(String::isEmpty)
//  Input("05-2-test.txt").contentRaw.chunkedByEmpty()
//
//  val c1 = cratesInput.dropLast(1).map { it.filterNot { it == '[' || it == ']' } }.map { it.replace(' ','.') }
//  println(c1)
//  val length = c1.maxBy { it.length }.length
//  println(length)
//  val c2 = c1.map { it.replace(' ','.') }.map { it.padEnd(length, '.') }.joinToString("\n")
//  println(c2)

}
