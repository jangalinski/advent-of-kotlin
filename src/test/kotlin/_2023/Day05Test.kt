package _2023

import io.github.jangalinski.aoc._2023.Day05.block
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


internal class Day05Test {

  @Test
  fun convert() {
    val convert = block("50 98 2")

    assertThat(convert(0)).isEqualTo(0L)
    assertThat(convert(98L)).isEqualTo(50L)

    assertThat(block("52 50 48")(53L)).isEqualTo(55L)
  }

  @Test
  fun `list convert`() {
    val x = block(
      listOf(
        "50 98 2",
        "52 50 48"
      )
    )

    assertThat(x(0L)).isEqualTo(0L)
    assertThat(x(48L)).isEqualTo(48L)
  }


  @ParameterizedTest
  @CsvSource(
    value = [
      "0,0",
      "1,1",
      "48,48",
      "49,49",
      "50,52",
      "51,53",
      "96,98",
      "97,99",
      "98,50",
      "99,51",
    ]
  )
  fun `seed soil map`(input: Long, expected: Long) {
    val x = block(
      listOf(
        "50 98 2",
        "52 50 48"
      )
    )
    assertThat(x(input)).isEqualTo(expected)
  }

  @Test
  fun `range plus`() {

    fun LongRange.plus(other: LongRange): List<LongRange> =
//    [a, c, d, b] = ab
      if (this.contains(other.first) && this.contains(other.last)) {
        listOf(this)
      }
//    [c, a, b, d] = cd
      else if (other.contains(this.first) && other.contains(this.last)) {
        listOf(other)
      }
//    [a, c, b, d] = ad
      else if (this.contains(other.first)) {
        listOf(LongRange(this.first, other.last))
      }
//    [c, a, d, b] = cb
      else if (other.contains(this.first)) {
        listOf(LongRange(other.first, this.last))
      } else {
        listOf(this, other)
      }

    operator fun List<LongRange>.plus(range: LongRange): List<LongRange> = fold(mutableListOf<LongRange>()) {a,c ->
      a.apply {
        addAll(c.plus(range))
      }
    }.distinct()

    fun List<LongRange>.reduce() : List<LongRange> {

      return this
    }


    assertThat(LongRange(1, 5).plus(LongRange(2, 4))).containsExactly(LongRange(1, 5))
    assertThat(LongRange(2, 3).plus(LongRange(1, 4))).containsExactly(LongRange(1, 4))
    assertThat(LongRange(2, 3).plus(LongRange(4, 5))).containsExactly(LongRange(2, 3), LongRange(4, 5))
    assertThat(LongRange(2, 5).plus(LongRange(4, 6))).containsExactly(LongRange(2, 6))
    assertThat(LongRange(4, 6).plus(LongRange(5, 7))).containsExactly(LongRange(4, 7))
    assertThat(LongRange(4, 6).plus(LongRange(5, 7))).containsExactly(LongRange(4, 7))

    assertThat(listOf(LongRange(1,2), LongRange(3,4)).plus(LongRange(5,6))).containsExactlyInAnyOrder(LongRange(1,2), LongRange(3,4), LongRange(5,6))

    assertThat(
      listOf(LongRange(1,5), LongRange(2,6))
    ).containsExactly(LongRange(1,6))


//
//    fun <E> List<E>.permutations(builtSequence: List<E> = listOf()): List<List<E>> =
//      if (isEmpty()) listOf(builtSequence)
//      else flatMap { (this - it).permutations(builtSequence + it) }
//
//    listOf("a","b","c","d").permutations()
//      .filter { it.indexOf("c") < it.indexOf("d") }
//      .filter { it.indexOf("a") < it.indexOf("b") }
//      .forEach { println(it) }
  }
}
