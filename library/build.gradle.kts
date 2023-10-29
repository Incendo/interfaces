dependencies {
    compileOnlyApi(libs.paper.api)
    compileOnlyApi(libs.guava)

    api("org.slf4j:slf4j-api:1.7.36")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.6")
}

