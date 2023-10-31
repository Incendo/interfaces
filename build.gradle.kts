import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import com.diffplug.gradle.spotless.SpotlessTask
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
    kotlin("jvm") version "1.8.21" apply false
    id("com.diffplug.spotless") version "6.18.0"
}

group = "org.incendo.interfaces"
version = "1.0.0-SNAPSHOT"
description = "A builder-style user interface library."

subprojects {
    apply<IndraPlugin>()
    apply<IndraCheckstylePlugin>()
    apply<SpotlessPlugin>()

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
            minimumToolchain(17)
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

    configure<SpotlessExtension> {
        kotlin {
            ktlint("0.47.1")
        }
    }

    // Configure any existing RunServerTasks
    tasks.withType<RunServerTask> {
        minecraftVersion("1.20.2")
        jvmArgs("-Dio.papermc.paper.suppress.sout.nags=true")
    }
}
