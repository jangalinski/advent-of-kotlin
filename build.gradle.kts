plugins {
  id("org.jetbrains.kotlin.jvm") version "1.7.22"
  application
}

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  implementation("io.toolisticon.lib:krid:0.0.1")
}


testing {
  suites {
    // Configure the built-in test suite
    val test by getting(JvmTestSuite::class) {
      // Use Kotlin Test test framework
      useKotlinTest("1.7.22")

      dependencies {
        // Use newer version of JUnit Engine for Kotlin Test
        implementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
      }
    }
  }
}

application {
  // Define the main class for the application.
  mainClass.set("io.github.jangalinski.aoc22.AppKt")
}
