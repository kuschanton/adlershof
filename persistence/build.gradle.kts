buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.2.5")
    }
}

plugins {
    id("org.flywaydb.flyway") version "5.2.4"
}

flyway {
    url = "jdbc:postgresql://localhost:5432/adlershof"
    user = "adlershof"
    password = "secret"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))

    implementation("org.springframework.data:spring-data-r2dbc:1.0.0.M1")
    implementation("io.r2dbc:r2dbc-postgresql:1.0.0.M7")
    implementation("org.flywaydb:flyway-core:3.0")
    implementation("org.postgresql:postgresql:42.2.5")

    implementation("io.projectreactor:reactor-core:3.2.11.RELEASE")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("javax.validation:validation-api:2.0.1.Final")

    testCompile("org.testcontainers:junit-jupiter:1.11.3")
}
