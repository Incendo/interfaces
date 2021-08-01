dependencies {
    api(projects.interfacesCore)

    compileOnlyApi(libs.adventure.api)
    compileOnlyApi(libs.paper.api) {
        isTransitive = false
    }
}
