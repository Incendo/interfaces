plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("org.jlleitschuh.gradle.ktlint")
}

dependencies {
    compileOnlyApi(libs.adventure.api)
    compileOnlyApi(libs.paper.api) {
        isTransitive = false
    }
    compileOnlyApi(libs.guava)
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

ktlint {
    version.set("0.42.1")
}

kotlin {
    explicitApi()
}
