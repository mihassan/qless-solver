rootProject.name = "qless-solver"

pluginManagement {
  plugins {
    val kotlinVersion = extra["kotlin.version"] as String
    kotlin("js") version kotlinVersion
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}
