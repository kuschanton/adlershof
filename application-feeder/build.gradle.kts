
plugins {
    id("org.springframework.boot") version "2.1.6.RELEASE"
    id("org.flywaydb.flyway") version "5.2.4"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":persistence"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}

