package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc.AoCUtil

fun main() {

  data class Resource(val ore: Int, val clay: Int, val obsidian: Int) {
    operator fun plus(res: Resource) = copy(ore = this.ore + res.ore, clay = this.clay + res.clay, obsidian = this.obsidian + res.obsidian)

    operator fun minus(res: Resource) = copy(ore = this.ore - res.ore, clay = this.clay - res.clay, obsidian = this.obsidian - res.obsidian)

    fun canAffort(res: Resource): Boolean {
      val (o, c, ob) = (this - res)
      return o >= 0 && c >= 0 && ob >= 0
    }
  }

  data class Blueprint(val id: Int, val oreRobot: Resource, val clayRobot: Resource, val obsidianRobot: Resource, val geodeRobot: Resource)

  fun input(line: String): Blueprint {
    val (id, oreOre, clayOre, obsidianOre, obsidianClay, geodeOre, geodeObsidian) = """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()
      .find(line)!!.destructured

    return Blueprint(
      id.toInt(),
      Resource(oreOre.toInt(), 0, 0),
      Resource(clayOre.toInt(), 0, 0),
      Resource(obsidianOre.toInt(), obsidianClay.toInt(), 0),
      Resource(geodeOre.toInt(), 0, geodeObsidian.toInt())
    )
  }

  fun input(test: Boolean) = AoCUtil.Input(year = 2022, day = 19, test = test).nonEmptyLines.map { input(it.trim()) }


  fun run(blueprint: Blueprint, factories: Resource) {

  }


  println(input(false))

  println("----")
  println(input(true))
}
