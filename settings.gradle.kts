plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "5.0.0"
}

rootProject.name = "interfaces"

interfacesProjects("core", "paper")

fun interfacesProjects(vararg names: String) {
    include(*names)

    names.forEach {
        project(":$it").name = "interfaces-$it"
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
