plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
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
    api(projects.interfacesCore)

    // Needed for Paper extensions.
    compileOnly(projects.interfacesPaper)
}
