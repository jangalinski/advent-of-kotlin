import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.jetbrains.kotlin.jvm") version "1.9.21"
}

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {

  //implementation("io.toolisticon.lib:krid:0.0.1")
  implementation("io.toolisticon.lib:krid:0.0.2-SNAPSHOT")
  implementation("io.github.microutils:kotlin-logging-jvm:3.0.2")
  implementation("com.github.freva:ascii-table:1.8.0")

  implementation("com.ibm.icu:icu4j:73.2")

  testImplementation(platform("org.junit:junit-bom:5.10.1"))
  testImplementation("org.assertj:assertj-core:3.23.1")
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
  kotlinOptions.languageVersion = "1.9"
}

tasks.test {
  useJUnitPlatform()
}

