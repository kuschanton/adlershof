import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

allprojects {

    group = "com.akushch"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "kotlin-spring")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf(
                "-Xjsr305=strict"
            )
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        // Spring
        implementation(platform("org.springframework.boot:spring-boot-dependencies:2.2.6.RELEASE"))
        implementation("org.springframework.boot:spring-boot-starter-validation")

        // Arrow
        implementation("io.arrow-kt:arrow-core-data")
        implementation("io.arrow-kt:arrow-effects-data")
        implementation("io.arrow-kt:arrow-effects-io-extensions")
        implementation("io.arrow-kt:arrow-extras-extensions")
        implementation("io.arrow-kt:arrow-effects-reactor-extensions")

        // Kotlin
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

        // Test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testImplementation("io.strikt:strikt-core")
        testImplementation("io.strikt:strikt-arrow")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }
}
