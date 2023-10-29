import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.repository.sonatypeSnapshots

plugins {
    alias(libs.plugins.indra)
    alias(libs.plugins.indra.publishing) apply false
    alias(libs.plugins.indra.publishing.sonatype)
    alias(libs.plugins.indra.checkstyle) apply false
    alias(libs.plugins.runPaper) apply false
}

group = "org.incendo.interfaces"
version = "2.0.0-SNAPSHOT"
description = "A builder-style user interface library."

subprojects {
    apply<IndraPlugin>()
    apply<IndraCheckstylePlugin>()

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
    }
}
