import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.6")
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

kotlin {
    explicitApi()
}
