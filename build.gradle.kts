import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.jetbrains.kotlin.jvm") version "1.7.22"
}

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  implementation("io.toolisticon.lib:krid:0.0.1")
  implementation("io.github.microutils:kotlin-logging-jvm:3.0.2")
  implementation("com.github.freva:ascii-table:1.8.0")

  testImplementation("org.assertj:assertj-core:3.23.1")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.1")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
  //kotlinOptions.languageVersion = "1.8"
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
