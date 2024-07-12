rootProject.name = "interfaces"

include("library")
include("examples")

project(":library").name = "interfaces-library"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
