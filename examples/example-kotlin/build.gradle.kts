plugins {
    kotlin("jvm")
    alias(libs.plugins.dokka)
    alias(libs.plugins.shadow)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.ktlint)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(projects.interfacesPaper)
    implementation(projects.interfacesKotlin)

    compileOnly(libs.paper.api)
}
