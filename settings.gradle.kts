plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "6.0.1"
}

rootProject.name = "interfaces"

include("library")
project(":library").name = "interfaces-library"

include("examples")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
