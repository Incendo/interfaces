dependencies {
    api(projects.interfacesCore)

    compileOnly(libs.adventure.api)
    compileOnly("net.kyori:adventure-text-serializer-gson:4.11.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.11.0")
    compileOnly(libs.paper.api) {
        isTransitive = false
    }
    compileOnly(libs.guava)
}
