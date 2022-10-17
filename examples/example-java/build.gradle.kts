plugins {
    alias(libs.plugins.runPaper)
}

dependencies {
    implementation(projects.interfacesPaper)

    compileOnly(libs.paper.api)
}
