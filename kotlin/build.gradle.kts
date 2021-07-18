plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.ncorti.ktfmt.gradle")
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
    dropboxStyle()
}

kotlin {
    explicitApi()
}

dependencies {
    api(projects.interfacesCore)
    api(projects.interfacesPaper)

    // Needed for Paper extensions.
    compileOnlyApi(libs.paper.api) {
        exclude(module = "guava")
        exclude(module = "gson")
        exclude(module = "snakeyaml")
        exclude(module = "commons-lang")
    }
}
