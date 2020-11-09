import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    kotlin("jvm") version "1.4.10"
    `java-library`
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.5"
}

repositories {
    jcenter()
}

group = "net.daverix.topologicalsorter"
version = "0.2"

dependencies {
    api(kotlin("stdlib-jdk8"))

    testImplementation("junit:junit:4.12")
    testImplementation("com.google.truth:truth:0.39")
}

tasks.withType<KotlinCompile<KotlinJvmOptions>> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    dependsOn("classes")
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

artifacts {
    archives(sourcesJar)
}

publishing {
    publications {
        register<MavenPublication>("lib") {
            from(components["java"])
            artifactId = "topologicalsorter"

            artifact(sourcesJar)

            pom {
                description.set("A node sorting library using depth first search")
            }
        }
    }
}

bintray {
    user = project.findProperty("bintrayUser").toString()
    key = project.findProperty("bintrayApikey").toString()
    setPublications("lib")

    pkg.apply {
        repo = "maven"
        name = "topologicalsorter"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/daverix/topologicalsorter.git"

        version.apply {
            name = "${project.version}"
            desc = "Topological sorter based on a depth first search algorithm"
            vcsTag = "v${project.version}"
        }
    }
}
