plugins {
    kotlin("jvm") version "2.3.10"
    application
    id("com.github.ben-manes.versions") version "0.53.0"
}

group = "org.maidavale"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

application {
    mainClass.set("org.maidavale.app.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

// Exclude pre-release versions from dependency update checks
tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        val preRelease = Regex("""(?i).*[.-](alpha|beta|rc|cr|m|preview|dev|snapshot)\d*.*""")
        preRelease.matches(candidate.version)
    }
}

// Fat jar — bundles all runtime dependencies into a single executable jar
tasks.jar {
    archiveBaseName.set(rootProject.name)
    manifest {
        attributes["Main-Class"] = "org.maidavale.app.MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
