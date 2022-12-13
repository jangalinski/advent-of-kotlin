package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil
import io.github.jangalinski.aoc.AoCUtil.KridExt.findByValue
import io.github.jangalinski.aoc.AoCUtil.KridExt.table
import io.github.jangalinski.aoc._2022.AoC202212.WalkingPath
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.getValue
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.set

object AoC202212 {

  enum class State {
    ACTIVE, FINISHED, STUCK
  }


  interface CanMoveTo : (Char, Char) -> Boolean

  object CanMoveToOnlyRising : CanMoveTo {
    override fun invoke(source: Char, destination: Char): Boolean {
      fun code(char: Char) = when (char) {
        HeightMap.START -> 'a'.code - 1
        HeightMap.GOAL -> 'z'.code + 1
        else -> char.code
      }

      return (code(destination) - code(source)) in 0..1
    }

//  infix fun Char.canMoveTo(other: Char): Boolean {
//    fun code(char: Char) = when (char) {
//      HeightMap.START -> 'a'.code - 1
//      HeightMap.GOAL -> 'z'.code + 1
//      else -> other.code
//    }
//
//    return (code(other) - code(this)) in 0..1
//  }

    infix fun Char.canMoveTo(other: Char): Boolean = other != HeightMap.START && when (this) {
      HeightMap.START -> true // anywhere from start
      HeightMap.GOAL -> false // nowhere from goal
      'z' -> true // z can fall to any

      else -> when (other) {
        HeightMap.GOAL -> false
        else -> other.code - this.code <= 1
      }
    }

    data class AllowedSteps(
      val map: ConcurrentMap<Pair<Cell, Cell>, Boolean>,
      val nextCells: ConcurrentMap<Cell, Set<Cell>> = ConcurrentHashMap()
    ) {

      constructor(krid: Krid<Char>) : this(
        map = krid.iterator().asSequence()
          .flatMap { base ->
            krid.orthogonalAdjacentCells(base.cell).map {
              base to krid.getValue(it)
            }
          }.fold(ConcurrentHashMap()) { map, (s, t) ->
            map.apply {
              map[s.cell to t.cell] = s.value canMoveTo t.value
            }
          }
      )

      init {
        calcNextCells()
      }

      operator fun get(cell: Cell): Set<Cell> = nextCells[cell] ?: emptySet()

      private fun calcNextCells() {
        nextCells.clear()
        nextCells.putAll(map.entries.filter { it.value }.map { it.key }.groupBy(keySelector = { it.first }) { it.second }
          .map { it.key to it.value.toSet() })
      }

      override fun toString(): String {

        return "AllowedSteps(map=$map, nextCells=$nextCells)"
      }
    }


    data class WalkingPath(
      val heightMap: HeightMap,
      val current: CellValue<Char>,
      val next: Set<Cell>,
      val passed: List<Cell>
    ) {
      companion object {
        operator fun invoke(heightMap: HeightMap): WalkingPath {
          val current = heightMap[heightMap.start]
          val next = heightMap.next(current.cell)
          return WalkingPath(
            heightMap = heightMap,
            current = current,
            next = next,
            passed = listOf(current.cell),

            )
        }
      }

      init {
        require(passed.isNotEmpty()) { "Path must include START." }
      }

      val state = if (HeightMap.GOAL == current.value) {
        State.FINISHED
      } else if (next.isEmpty()) {
        State.STUCK
      } else {
        State.ACTIVE
      }

      fun walkTo(cell: Cell): WalkingPath {
        require(next.contains(cell)) { "Cannot walk to $cell - not in $next." }
        val destination = heightMap[cell]
        val followUp = heightMap.next(cell).subtract(passed.toSet())

        return copy(current = destination, next = followUp, passed = passed + cell)
      }

      val length = passed.size

      override fun toString(): String {
        return "Path(current=$current, next=$next, passed=$passed, state=$state, length=$length)"
      }
    }

    data class HeightMap(val krid: Krid<Char>, val steps: AllowedSteps) {

      constructor(krid: Krid<Char>) : this(krid = krid, steps = AllowedSteps(krid))

      companion object {
        const val START = 'S'
        const val GOAL = 'E'
      }

      fun startWalking() = WalkingPath(heightMap = this)

      val start = krid.findByValue(START)
      val goal = krid.findByValue(GOAL)

      operator fun get(cell: Cell): CellValue<Char> = krid.getValue(cell)

      override fun toString() = "HeightMap(krid=$krid, start=$start, goal=$goal)"

      fun next(cell: Cell): Set<Cell> = steps[cell]
    }
  }

  object CanMoveToAndFallDown : CanMoveTo {
    override fun invoke(source: Char, destination: Char): Boolean = destination != HeightMap.START && when (source) {
      HeightMap.START -> true // anywhere from start
      HeightMap.GOAL -> false // nowhere from goal
      'z' -> true // z can fall to any

      else -> when (destination) {
        HeightMap.GOAL -> false
        else -> destination.code - source.code <= 1
      }
    }
  }

  data class AllowedSteps(
    val map: ConcurrentMap<Pair<Cell, Cell>, Boolean>,
    val nextCells: ConcurrentMap<Cell, Set<Cell>> = ConcurrentHashMap()
  ) {

    constructor(krid: Krid<Char> ,canMoveTo: CanMoveTo = CanMoveToAndFallDown) : this(
      map = krid.iterator().asSequence()
        .flatMap { base ->
          krid.orthogonalAdjacentCells(base.cell).map {
            base to krid.getValue(it)
          }
        }.fold(ConcurrentHashMap()) { map, (s, t) ->
          map.apply {
            map[s.cell to t.cell] = canMoveTo(s.value,t.value)
          }
        }
    )

    init {
      calcNextCells()
    }

    operator fun get(cell: Cell): Set<Cell> = nextCells[cell] ?: emptySet()

    private fun calcNextCells() {
      nextCells.clear()
      nextCells.putAll(map.entries.filter { it.value }.map { it.key }.groupBy(keySelector = { it.first }) { it.second }
        .map { it.key to it.value.toSet() })
    }

    override fun toString(): String {

      return "AllowedSteps(map=$map, nextCells=$nextCells)"
    }
  }


  data class WalkingPath(
    val heightMap: HeightMap,
    val current: CellValue<Char>,
    val next: Set<Cell>,
    val passed: List<Cell>
  ) {
    companion object {
      operator fun invoke(heightMap: HeightMap): WalkingPath {
        val current = heightMap[heightMap.start]
        val next = heightMap.next(current.cell)
        return WalkingPath(
          heightMap = heightMap,
          current = current,
          next = next,
          passed = listOf(current.cell),

          )
      }
    }

    init {
      require(passed.isNotEmpty()) { "Path must include START." }
    }

    val state = if (HeightMap.GOAL == current.value) {
      State.FINISHED
    } else if (next.isEmpty()) {
      State.STUCK
    } else {
      State.ACTIVE
    }

    fun walkTo(cell: Cell): WalkingPath {
      require(next.contains(cell)) { "Cannot walk to $cell - not in $next." }
      val destination = heightMap[cell]
      val followUp = heightMap.next(cell).subtract(passed.toSet())

      return copy(current = destination, next = followUp, passed = passed + cell)
    }

    val length = passed.size

    override fun toString(): String {
      return "Path(current=$current, next=$next, passed=$passed, state=$state, length=$length)"
    }
  }

  data class HeightMap(val krid: Krid<Char>, val steps: AllowedSteps) {

    constructor(krid: Krid<Char>) : this(krid = krid, steps = AllowedSteps(krid))

    companion object {
      const val START = 'S'
      const val GOAL = 'E'
    }

    fun startWalking() = WalkingPath(heightMap = this)

    val start = krid.findByValue(START)
    val goal = krid.findByValue(GOAL)

    operator fun get(cell: Cell): CellValue<Char> = krid.getValue(cell)

    override fun toString() = "HeightMap(krid=$krid, start=$start, goal=$goal)"

    fun next(cell: Cell): Set<Cell> = steps[cell]
  }
}

fun main() {
  val heightMap = AoC202212.HeightMap(AoCUtil.Input(year = 2022, day = 12, test = true).krid())

  fun part1(): Int {
    data class Result(
      val min: Int = Integer.MAX_VALUE,

      )

    val count = AtomicInteger(0)

    sequence<WalkingPath> {
      var best : Int = -1

      fun walk(p: WalkingPath) {
        if (p.state != AoC202212.State.ACTIVE) {
          //yield(p)
        }
      }
    }


    fun walk(path: WalkingPath, all: List<WalkingPath> = emptyList()): List<WalkingPath> {
      count.incrementAndGet()
      if (path.state != AoC202212.State.ACTIVE) {
        return all + path
      }

      return path.next.map { path.walkTo(it) }.flatMap { walk(it) }
    }

    val x = walk(heightMap.startWalking()).filter { AoC202212.State.FINISHED == it.state }
      .sortedBy { it.length }


  println("iterations: ${count.get()}")
    return x.first().length - 1
  }

  println(heightMap)
  println(heightMap.krid.table())

  println("PART1: ${part1()}")


}
