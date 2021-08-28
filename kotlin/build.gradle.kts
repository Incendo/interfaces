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

    // Needed for Paper extensions.
    implementation(projects.interfacesPaper)
}
