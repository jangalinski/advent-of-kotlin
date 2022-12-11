package io.github.jangalinski.aoc._2022

import io.github.jangalinski.aoc._2022.Aoc2022_07.FileItem.Directory
import io.github.jangalinski.aoc._2022.Aoc2022_07.isCd
import io.github.jangalinski.aoc.Input

object Aoc2022_07 {

  fun parseCommand(cmd: String): List<Command> = if (cmd.startsWith("cd ")) {
    listOf(
      when (val dirName = cmd.removePrefix("cd ")) {
        "/" -> Command.CD_ROOT
        ".." -> Command.CD_UP
        else -> Command.CD(dirName)
      }
    )
  } else if (cmd.startsWith("ls")) {
    cmd.removePrefix("ls\n").lines().map { line ->
      if (line.startsWith("dir ")) {
          Command.MKDIR(line.removePrefix("dir "))
      } else {
        val (s, n) = line.split(" ")
          Command.TOUCH(n, s.toInt())
      }
    }
  } else {
    TODO()
  }

  fun List<Command>.isCd() = all { it is Command.CD_UP || it is Command.CD || it is Command.CD_ROOT }

  sealed interface FileItem {
    val name: String
    val size: Int

    data class Directory(
      override val name: String,
      val path: String,
      val subDirectories: MutableList<Directory> = mutableListOf(),
      val files: MutableList<File> = mutableListOf()
    ) : FileItem {

      override val size: Int = subDirectories.sumOf { it.size } + files.sumSize()
    }

    data class File(override val name: String, override val size: Int) : FileItem
  }



  fun List<FileItem.File>.sumSize() = this.sumOf { it.size }

  sealed interface Command {

    data class CD(val name: String) : Command

    // TODO try data objects , language level 1.9
    object CD_ROOT : Command {
      override fun toString() = "CD(/)"
    }

    object CD_UP : Command {
      override fun toString() = "CD(..)"
    }

    data class TOUCH(val name: String, val size: Int) : Command {
      operator fun invoke() = FileItem.File(name, size)
    }

    data class MKDIR(val name: String) : Command {
      fun create(path: String) = Directory(name = name, path = "$path/$name")
    }
  }

}


fun main() {


  val input = Input("07-1-test.txt").contentRaw.split("\$ ")
    .mapNotNull { line ->
      line.trim().let {
        it.ifEmpty {
          null
        }
      }
    }

  val commands = input.map { Aoc2022_07.parseCommand(it) }

  val fs = Directory("/", "")
  val path = mutableListOf<String>()
  var current = fs

  fun state() = println(
    """
    STATE:
    fs=$fs
    pwd=/${path.joinToString("/")}
    current=$current
  """.trimIndent()
  )

  commands.forEach { cmds ->
    if (cmds.isCd()) {
      when (val cmd = cmds.single()) {
        is Aoc2022_07.Command.CD_ROOT -> path.clear()
        is Aoc2022_07.Command.CD_UP -> path.apply { removeLast() }
        is Aoc2022_07.Command.CD -> path.apply { add(cmd.name) }
        else -> TODO("no a CD cmd")
      }

      current = path.fold(fs) { c, n ->
        c.subDirectories.singleOrNull { it.name == n } ?: throw IllegalArgumentException("dir not found $n")
      }
    } else {
      cmds.forEach { cmd ->
        when (cmd) {
          is Aoc2022_07.Command.TOUCH -> current.files.add(cmd())
          is Aoc2022_07.Command.MKDIR -> current.subDirectories.add(cmd.create(current.path))
          else -> TODO("no create command $cmd")
        }
      }
    }
  }

  state()

  println("fs ${fs.size}")


  fun subDirs(d: Directory ) = sequence<Int> {
    yield(d.size)

    d.subDirectories.forEach {
      yield(it.size)

    }
  }

  subDirs(fs).forEach { println(it) }

  val root = fs.path to fs.size

  val dirs = fs.subDirectories.flatMap {
    it.subDirectories
  }.flatMap { it.subDirectories }
    .flatMap { it.subDirectories }
    .map { it.path to it.size }.toMap().toMutableMap().apply {
      put(root.first, root.second)
    }

println (dirs)
}
