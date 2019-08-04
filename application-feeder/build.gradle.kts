//import org.springframework.boot.gradle.tasks.bundling.BootJar
//
plugins {
    id("org.springframework.boot") version "2.1.6.RELEASE"
}

dependencies {
    implementation(project(":domain"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
//    implementation("org.springframework.boot:spring-boot-starter-web") {
//        exclude(
//            group = "org.springframework.boot",
//            module = "spring-boot-starter-tomcat"
//        )
//    }
//    implementation("org.springframework.boot:spring-boot-starter-undertow")
}

