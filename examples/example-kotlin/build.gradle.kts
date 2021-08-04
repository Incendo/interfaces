plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.ncorti.ktfmt.gradle")
    id("com.github.johnrengelman.shadow")
    id("xyz.jpenilla.run-paper")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}

ktfmt {
    kotlinLangStyle()
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(projects.interfacesPaper)
    implementation(projects.interfacesKotlin)

    compileOnly(libs.paper.api)
}
