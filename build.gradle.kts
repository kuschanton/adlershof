import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    kotlin("plugin.spring") version "1.3.21"
//    id("org.springframework.boot") version "2.1.6.RELEASE"
//    id("io.spring.dependency-management") version "1.0.7.RELEASE"
////    id("org.owasp.dependencycheck")
//    id("org.jmailen.kotlinter") version "1.22.0"
}

allprojects {
    group = "com.akushch"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "kotlin-spring")
//    apply(plugin = "org.springframework.boot")   // ???
//    apply(plugin = "org.flywaydb.flyway")        // ???

//    apply(plugin = "org.owasp.dependencycheck")
//    apply(plugin = "org.jmailen.kotlinter")

    repositories {
        mavenCentral()
        maven(url = "http://repo.spring.io/milestone")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf(
                "-Xjsr305=strict"
            )
        }
    }

    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:2.1.6.RELEASE"))
//        implementation("org.springframework.boot:spring-boot-configuration-processor")

        // Arrow
        implementation("io.arrow-kt:arrow-core-data")
        implementation("io.arrow-kt:arrow-effects-data")
        implementation("io.arrow-kt:arrow-effects-io-extensions")
        implementation("io.arrow-kt:arrow-extras-extensions")
        implementation("io.arrow-kt:arrow-effects-reactor-extensions")

        // Kotlin
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-RC")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.0-RC")

//    kapt {
//        correctErrorTypes = true
//        includeCompileClasspath = false
//    }
//
//    tasks.withType<KotlinCompile> {
//        kotlinOptions {
//            freeCompilerArgs = listOf("-Xjsr305=strict")
//            jvmTarget = "1.8"
//        }
//    }

//    dependencyCheck {
//        format = XML
//    }

//    dependencies {
//        implementation("org.springframework.boot:spring-boot-starter:2.1.5.RELEASE")
//        kapt("org.springframework.boot:spring-boot-configuration-processor")
//        implementation("org.jetbrains.kotlin:kotlin-reflect")
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//        implementation("org.jetbrains.kotlinx:kotlin-coroutines-core")
//        implementation("org.jetbrains.kotlinx:kotlin-coroutines-reactor:1.2.1")
//
//        testImplementation("org.springframework.boot:spring-boot-starter-test")
//
//        implementation("org.jetbrains.kotlin:kotlin-reflect")
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//        implementation("org.jetbrains.kotlinx:kotlin-coroutines-core")
//        implementation("org.jetbrains.kotlinx:kotlin-coroutines-reactor:1.2.1")
//
//        implementation("org.springframework.boot:spring-boot-starter-webflux")
//        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//        runtimeOnly("org.postgresql:postgresql")
//        testImplementation("org.springframework.boot:spring-boot-starter-test")
//        testImplementation("io.projectreactor:reactor-test")
//    }

//    sourceSets["main"].java.srcDir("$buildDir/tmp/kapt3/calsses/main")
    }
}
