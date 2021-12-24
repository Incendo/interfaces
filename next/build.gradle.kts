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
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}

ktlint {
    version.set("0.42.1")
}

kotlin {
    explicitApi()
}
