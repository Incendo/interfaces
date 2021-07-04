plugins {
    kotlin("jvm") version "1.4.31"
    id("org.jetbrains.dokka")
    id("com.ncorti.ktfmt.gradle")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.11"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.11"
    }
}

ktfmt {
    dropboxStyle()
}

kotlin {
    explicitApi()
}

dependencies {
    api(project(":interfaces-core"))
    api(project(":interfaces-paper"))

    // Needed for Paper extensions.
    compileOnlyApi(libs.paper.api) {
        exclude(module = "guava")
        exclude(module = "gson")
        exclude(module = "snakeyaml")
        exclude(module = "commons-lang")
    }
}
