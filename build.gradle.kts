import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.repository.sonatypeSnapshots

plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.publishing") apply false
    id("net.kyori.indra.checkstyle") apply false
    id("xyz.jpenilla.run-paper") apply false

    // Auto-generates project files.
    idea
}

group = "org.incendo.interfaces"
version = "1.0.0-SNAPSHOT"
description = "A builder-style user interface library."

subprojects {
    apply<IndraPlugin>()
    apply<IndraPublishingPlugin>()
    apply<IndraCheckstylePlugin>()

    repositories {
        mavenCentral()
        sonatypeSnapshots()
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    dependencies {
        compileOnly(rootProject.libs.checker.qual)
    }

    indra {
        mitLicense()

        publishSnapshotsTo("incendo", "https://repo.incendo.org/content/repositories/snapshots/")

        javaVersions {
            target(11)
        }

        github("incendo", "interfaces") {
            ci(true)
        }

        configurePublications {
            pom {
                developers {
                    developer {
                        id.set("kadenscott")
                        email.set("kscottdev@gmail.com")
                    }
                }
            }
        }
    }
}
