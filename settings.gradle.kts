plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "5.0.0"
}

rootProject.name = "interfaces"

interfacesProjects("next")

fun interfacesProjects(vararg names: String) {
    include(*names)

    names.forEach {
        project(":$it").name = "interfaces-$it"
    }
}

// Add the example modules.
//include("examples/example-java")
//project(":examples/example-java").name = "example-java"
//include("examples/example-kotlin")
//project(":examples/example-kotlin").name = "example-kotlin"
include("examples/example-next")
project(":examples/example-next").name = "example-next"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
