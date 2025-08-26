import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    alias(libs.plugins.dokka)
    alias(libs.plugins.ktlint)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

ktlint {
    version.set("1.7.1")
}

kotlin {
    explicitApi()
}

dependencies {
    api(projects.interfacesCore)

    // Needed for Paper extensions.
    compileOnly(projects.interfacesPaper)
    compileOnly(libs.adventure.api)
    compileOnly(libs.paper.api)
}
