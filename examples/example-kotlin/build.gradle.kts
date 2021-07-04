plugins {
    kotlin("jvm") version "1.4.31"
    id("org.jetbrains.dokka")
    id("com.ncorti.ktfmt.gradle")
    id("com.github.johnrengelman.shadow")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.11"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.11"
    }
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":interfaces-core"))
    implementation(project(":interfaces-paper"))
    implementation(project(":interfaces-kotlin"))

    // Needed for Paper extensions.
    compileOnlyApi(libs.paper.api) {
        exclude(module = "guava")
        exclude(module = "gson")
        exclude(module = "snakeyaml")
        exclude(module = "commons-lang")
    }
}
