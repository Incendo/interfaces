plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow")
    id("xyz.jpenilla.run-paper")
    id("org.jlleitschuh.gradle.ktlint")
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

dependencies {
    implementation(projects.interfacesPaper)
    implementation(projects.interfacesKotlin)

    compileOnly(libs.paper.api)
}
