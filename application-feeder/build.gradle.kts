import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-log4j")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

tasks.withType<BootJar> {
    archiveBaseName.set("adlershof-feeder")
    archiveClassifier.set("exec")
}
