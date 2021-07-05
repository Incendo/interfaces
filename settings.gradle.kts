plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "5.0.0"
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
include("examples/example-kotlin")
project(":examples/example-kotlin").name = "example-kotlin"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
