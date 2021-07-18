dependencies {
    api(projects.interfacesCore)

    compileOnlyApi(libs.adventure.api)
    compileOnlyApi(libs.paper.api) {
        exclude(module = "guava")
        exclude(module = "gson")
        exclude(module = "snakeyaml")
        exclude(module = "commons-lang")
    }
}
