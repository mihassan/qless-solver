plugins {
    kotlin("js")
}

fun kotlinw(target: String): String =
    "org.jetbrains.kotlin-wrappers:kotlin-$target"

val kotlinWrappersVersion = "1.0.0-pre.568"

dependencies {
    testImplementation(kotlin("test"))

    implementation(enforcedPlatform(kotlinw("wrappers-bom:$kotlinWrappersVersion")))

    implementation(kotlinw("react"))
    implementation(kotlinw("react-dom"))
    implementation(kotlinw("emotion"))
    implementation(kotlinw("mui"))
    implementation(kotlinw("mui-icons"))
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "7.6"
    }
}
