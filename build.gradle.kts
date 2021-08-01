import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.repository.sonatypeSnapshots
import xyz.jpenilla.runpaper.task.RunServerTask

plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.publishing") apply false
    id("net.kyori.indra.checkstyle") apply false
    id("xyz.jpenilla.run-paper") apply false

    // Kotlin plugin prefers to be applied to parent when it's used in multiple projects.
    kotlin("jvm") version "1.4.31" apply false
}

group = "org.incendo.interfaces"
version = "1.0.0-SNAPSHOT"
description = "A builder-style user interface library."

subprojects {
    apply<IndraPlugin>()
    apply<IndraCheckstylePlugin>()

    // Don't publish examples
    if (!name.startsWith("example-")) apply<IndraPublishingPlugin>()

    repositories {
        mavenCentral()
        sonatypeSnapshots()
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    dependencies {
        compileOnlyApi(rootProject.libs.checker.qual)
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

    // Configure any existing RunServerTasks
    tasks.withType<RunServerTask> {
        minecraftVersion("1.17.1")
        jvmArgs("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005")
    }
}
