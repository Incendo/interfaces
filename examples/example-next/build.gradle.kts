plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow")
    id("xyz.jpenilla.run-paper")
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

dependencies {
    implementation(projects.interfacesNext)
    implementation("cloud.commandframework", "cloud-paper", "1.7.1")
    implementation("cloud.commandframework", "cloud-kotlin-extensions", "1.7.1")
    implementation("cloud.commandframework", "cloud-kotlin-coroutines", "1.7.1")

    compileOnly(libs.paper.api)
}
