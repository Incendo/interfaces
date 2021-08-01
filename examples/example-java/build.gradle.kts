plugins {
    id("xyz.jpenilla.run-paper")
}

dependencies {
    implementation(projects.interfacesPaper)

    compileOnly(libs.paper.api)
}
