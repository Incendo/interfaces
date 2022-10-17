plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "6.0.1"
}

rootProject.name = "interfaces"

interfacesProjects("core", "paper", "kotlin")

fun interfacesProjects(vararg names: String) {
    include(*names)

    names.forEach {
        project(":$it").name = "interfaces-$it"
    }
}

// Add the example modules.
include("examples/example-java")
project(":examples/example-java").name = "example-java"
include("examples/example-kotlin")
project(":examples/example-kotlin").name = "example-kotlin"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
