import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.repository.sonatypeSnapshots
import xyz.jpenilla.runpaper.task.RunServerTask

plugins {
    alias(libs.plugins.indra)
    alias(libs.plugins.indra.publishing) apply false
    alias(libs.plugins.indra.publishing.sonatype)
    alias(libs.plugins.indra.checkstyle) apply false
    alias(libs.plugins.runPaper) apply false

    // Kotlin plugin prefers to be applied to parent when it's used in multiple sub-modules.
    kotlin("jvm") version "1.7.10" apply false
    alias(libs.plugins.ktlint) apply false
}

group = "org.incendo.interfaces"
version = "1.0.0-SNAPSHOT"
description = "A builder-style user interface library."

subprojects {
    apply<IndraPlugin>()
    apply<IndraCheckstylePlugin>()

    // Don't publish examples
    if (!name.startsWith("example-")) {
        apply<IndraPublishingPlugin>()
    }

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

        javaVersions {
            target(17)
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
        minecraftVersion("1.19.2")
        jvmArgs(
            "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005",
            "-Dio.papermc.paper.suppress.sout.nags=true")
    }
}
