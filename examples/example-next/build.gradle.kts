plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow")
    id("xyz.jpenilla.run-paper")
    id("org.jlleitschuh.gradle.ktlint")
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

dependencies {
    implementation(projects.interfacesNext)
    implementation("cloud.commandframework", "cloud-paper", "1.7.1")
    implementation("cloud.commandframework", "cloud-kotlin-extensions", "1.7.1")
    implementation("cloud.commandframework", "cloud-kotlin-coroutines", "1.7.1")

    compileOnly(libs.paper.api)
}
