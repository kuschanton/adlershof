//pluginManagement {
//    repositories {
//        gradlePluginPortal()
//    }
//
//    resolutionStrategy {
//        eachPlugin {
//            when (requested.id.namespace) {
//                "org.jetbrains.kotlin.plugin" -> useVersion("1.3.21")
//                "org.jetbrains.kotlin" -> useVersion("1.3.21")
//            }
//
//            when (requested.id.id) {
////                "org.owasp.dependencycheck" -> useVersion("5.0.0-M2")
//                "org.springframework.boot" -> useVersion("2.1.5.RELEASE")
////                "org.jmailen.kotlinter" -> useVersion("1.22.0")
//            }
//        }
//    }
//}
//
gradle.allprojects {
    configurations.all {
        resolutionStrategy {
//            force("org.springframework.boot:spring-boot-configuration-processor:2.1.3.RELEASE")
//            force("org.springframework.boot:spring-boot-starter-log4j:1.3.8.RELEASE")

            eachDependency {
                when(requested.group) {
                    "io.arrow-kt" -> useVersion("0.9.0")
                }
            }
        }
    }
}

rootProject.name = "adlershof"

include(
    ":application-feeder",
    ":domain",
    ":tankerkoenig-client"
)

project(":tankerkoenig-client").projectDir =
    file("client/tankerkoenig-client")
