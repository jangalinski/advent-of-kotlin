package io.github.jangalinski.aoc._2023

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.ListExt.head
import io.github.jangalinski.aoc.AoCUtil.StringExt.longValues
import io.github.jangalinski.aoc.AoCUtil.StringExt.toPair

object Day05 {


  fun single(line: String): (Long) -> Pair<Long, Boolean> {
    val (dest, source, size) = line.longValues()
    return single(dest, source, size)
  }

  fun single(destStart: Long, sourceStart: Long, size: Long): (Long) -> Pair<Long, Boolean> = object : (Long) -> Pair<Long, Boolean> {
    val range = LongRange(start = sourceStart, endInclusive = sourceStart + (size - 1))

    val mem = mutableMapOf<Long, Pair<Long,Boolean>>()


    override fun invoke(num: Long): Pair<Long, Boolean> = mem.computeIfAbsent(num) {_ ->
      if (range.contains(num)) {
        (destStart + range.indexOf(num)) to true
      } else {
        num to false
      }
    }

    override fun toString(): String {
      return "$destStart, $sourceStart, $size, range=$range"
    }
  }

  fun all(blocks: List<(Long) -> Long>): (Long) -> Long = {
    blocks.fold(it) { a, c -> c(a) }
  }

  fun block(line: String) = block(listOf(line))
  fun block(lines: List<String>): (Long) -> Long = blockConversion(lines.map { single(it) })

  //fun convert(fns: List<List<(Long) -> Pair<Long, Boolean>>>) : (Long) ->Long = {fns.fold(it) {a,c -> c(a)} }

  fun blockConversion(converts: List<(Long) -> Pair<Long, Boolean>>): (Long) -> Long = {
    converts.fold(it to false) { a, c ->
      if (a.second) {
        a
      } else {
        c(a.first)
      }
    }.first
  }

}

fun main() {


  fun parse(input: AoCUtil.Input): Pair<List<Long>, (Long) -> Long> {
    val chunks = input.linesChunkedByEmpty().head()
    val seeds = chunks.first.single().toPair(":").second.longValues()

    val blocks = chunks.second.map {
      it.head().second
    }.map { Day05.block(it) }

    return seeds to {
      blocks.fold(it) { acc, b ->
        b(acc).also {
          //println("in: $acc, block: $b, res: $it")
        }
      }
    }
  }


  val test = AoCUtil.Input(year = 2023, day = 5, test = false)

  val (seeds, fn) = parse(test)


  // silver
//  println(seeds.map {
//    it to fn(it)
//  }.minBy { it.second }.second)

  // gold
  val seedRanges = seeds.chunked(2).map { (s, l) -> LongRange(s, s + l - 1) }
//    .fold(mutableSetOf<Long>()) { a, c ->
//    a.apply {
//      c.forEach { a.add(it) }
//    }
//  }.toList().sorted()


//  0006434225..0298276998
//  1365412380..1445496559
//  1515742281..1689752260
//  1692203766..2035017732
//  2276375722..2436523853
//  2590548294..3180906054
//  3289792522..3393308608
//  3424292843..3506403139
//  3574751516..4159532651
//  4207087048..4243281403

  println(LongRange(6434225,298276998).asSequence()
    .map {
    it to fn(it)
  }.minBy { it.second }.second)



  //println(seeds.minBy { fn(it) })

}
