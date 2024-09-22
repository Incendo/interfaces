plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

repositories {
    maven("https://repo.broccol.ai/snapshots")
}

dependencies {
    implementation(projects.interfacesLibrary)
    implementation(libs.cloud.paper)
    implementation(libs.corn.minecraft)
}

tasks {
    runServer {
        minecraftVersion("1.21")
    }
}
