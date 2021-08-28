dependencies {
    api(projects.interfacesCore)

    compileOnlyApi(libs.adventure.api)
    compileOnly(libs.paper.api) {
        isTransitive = false
    }
    compileOnlyApi(libs.guava)
}
