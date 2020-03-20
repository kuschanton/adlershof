buildscript {
    dependencies {
        // TODO: How to manage version via resolutions?
        classpath("org.postgresql:postgresql:42.2.5")
    }
}

plugins {
    id("org.flywaydb.flyway")
}

flyway {
    url = "jdbc:postgresql://localhost:5432/adlershof"
    user = "adlershof"
    password = "secret"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))

    implementation("org.springframework.data:spring-data-r2dbc")
    implementation("io.r2dbc:r2dbc-postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")

    implementation("io.projectreactor:reactor-core")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.github.database-rider:rider-junit5")

    // Only to run migration because r2dbc does not work with flyway yet
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc")
}
