plugins {
    kotlin("jvm")
    alias(libs.plugins.dokka)
    alias(libs.plugins.shadow)
    alias(libs.plugins.runPaper)
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
