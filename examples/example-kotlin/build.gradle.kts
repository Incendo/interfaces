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
