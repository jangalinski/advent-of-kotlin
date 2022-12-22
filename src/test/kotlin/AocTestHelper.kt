package io.github.jangalinski.aoc

import io.toolisticon.lib.krid.model.Cell
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.converter.ArgumentConverter

object AocTestHelper {

  class ToCell : ArgumentConverter {
    override fun convert(s: Any, ctx: ParameterContext?): Cell {
      require(s is String && s.startsWith("(") && s.endsWith(")")) { "s has to be '(1,2)'" }

      val (x, y) = s.removePrefix("(").removeSuffix(")").split(",").map { it.trim().toInt() }
      return Cell(x, y)
    }

  }

  class ToIntList : ArgumentConverter {
    override fun convert(csv: Any, ctx: ParameterContext): List<Int> {
      require(csv is String)
      return csv.split(",").map(String::toInt)
    }

  }

}
