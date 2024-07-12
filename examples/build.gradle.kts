plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

dependencies {
    implementation(projects.interfacesLibrary)
    implementation(libs.cloud.paper)
}

tasks {
    runServer {
        minecraftVersion("1.21")
    }
}
