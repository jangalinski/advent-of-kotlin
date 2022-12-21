package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil

object Aoc202216 {

  sealed interface Action {

    val name: String

    data class Walk(override val name: String) : Action

    data class Open(override val name: String) : Action
  }

  data class Actions(
    val current: Valve,
    val valves: Map<String, Valve>,
    val list: List<Action> = listOf()
  ) {


    operator fun get(name: String): Valve = requireNotNull(valves[name])

    val isOpen: Boolean by lazy {
      list.any { it == Action.Open(current.name) }
    }

    fun possibleMoves(): List<Action> =
      current.tunnels.map { Action.Walk(it) } + (if (!isOpen && current.rate > 0) listOf(Action.Open(current.name)) else emptyList())



    fun doMoves(): List<Actions> = possibleMoves().map {

      this.copy(current = if (it is Action.Walk) get(it.name) else current, list = this.list + it)
    }

    val size = list.size

    override fun toString(): String {
      return "Actions(current=$current, isOpen=$isOpen, size=$size, list=$list)"
    }
  }

  data class Valve(val name: String, val rate: Int, val tunnels: List<String>)

  fun String.parseValve(): Valve {
    val x = this.replace(", ", ",")
      .replace("Valve ", "")
      .replace(" has flow rate", "")
      .replace("; tunnel", "=")
      .replace(" ", "=").split("=")

    val v = x[0]
    val r = x[1].toInt()
    val t = x.last().split(",")

    return Valve(name = v, rate = r, tunnels = t)
  }

  fun input(test: Boolean) = AoCUtil.Input(year = 2022, day = 16, test = test).nonEmptyLines
    .map {
      it.parseValve()
    }
}

fun main() {


  fun part1(test: Boolean): Int {
    val input = Aoc202216.input(test)

    val valves = input.associateBy { it.name }
    val states = input.flatMap { it.tunnels + it.name }.distinct().associateBy({ it }) { -1 }

    val result = mutableListOf<Aoc202216.Actions>()


    val start: Aoc202216.Actions = Aoc202216.Actions(current = valves["AA"]!!, valves=valves)

    println(start)
    println(start.possibleMoves())
    println()
    println(start.doMoves())




    fun iterate(actions: Aoc202216.Actions, result : List<Aoc202216.Actions> = emptyList()) : List<Aoc202216.Actions> {
      if (actions.size == 30) {
        return result + actions
      }

      println(actions)
      return actions.doMoves().flatMap { iterate(it, result) }
    }

    println(iterate(actions = start))

    return -1
  }

  fun part2(): Int {
    return -1
  }

  println(part1(true))
}
