plugins {
    kotlin("jvm")
    alias(libs.plugins.dokka)
}

dependencies {
    compileOnlyApi(libs.adventure.api)
    compileOnlyApi(libs.paper.api) {
        isTransitive = false
    }
    compileOnlyApi(libs.guava)
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    api("org.slf4j:slf4j-api:1.7.36")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

kotlin {
    explicitApi()
}
