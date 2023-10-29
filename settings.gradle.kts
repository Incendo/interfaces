plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "6.0.1"
}

rootProject.name = "interfaces"

include("library")
include("examples")

project(":library").name = "interfaces-library"

