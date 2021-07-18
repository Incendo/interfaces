plugins {
    id("xyz.jpenilla.run-paper")
}

dependencies {
    implementation(projects.interfacesCore)
    implementation(projects.interfacesPaper)

    compileOnlyApi(libs.paper.api) {
        exclude(module = "guava")
        exclude(module = "gson")
        exclude(module = "snakeyaml")
        exclude(module = "commons-lang")
    }
}

tasks {
    runServer {
        minecraftVersion("1.16.5")
    }
}
