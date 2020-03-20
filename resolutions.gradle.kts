var kotlinVersion = "1.3.70"
var coroutinesVersion = "1.3.4"
var springBootVersion = "2.2.5.RELEASE"
var arrowVersion = "0.9.0"
var striktVersion = "0.24.0"
var junitVersion = "5.5.1"
var flywayVersion = "5.2.4"
var jacksonVersion = "2.9.9"
var postgreVersion = "42.2.5"
var springDataR2dbcVersion = "1.0.0.RELEASE"
var r2dbcPostgreVersion = "0.8.1.RELEASE"
var reactorVersion = "3.3.3.RELEASE"
var reactorKotlinExtensionsVersion = "1.0.2.RELEASE"
var testContainersVersion = "1.11.3"
var dbRiderVersion = "1.7.2"

gradle.settingsEvaluated {
    pluginManagement {
        resolutionStrategy {
            eachPlugin {
                when (requested.id.id) {
                    "org.jetbrains.kotlin.jvm" ->  useVersion(kotlinVersion)
                    "org.jetbrains.kotlin.plugin.spring" ->  useVersion(kotlinVersion)
                    "org.springframework.boot" -> useVersion(springBootVersion)
                    "org.flywaydb.flyway" -> useVersion(flywayVersion)
                    "org.postgresql" -> useVersion(postgreVersion)
                }
            }
        }
    }
}

gradle.allprojects {
    configurations.all {
        resolutionStrategy {
            eachDependency {
                when(requested.group) {
                    // Kotlin
                    "org.jetbrains.kotlin" -> useVersion(kotlinVersion)
                    "org.jetbrains.kotlinx" -> useVersion(coroutinesVersion)

                    // Other
                    "io.arrow-kt" -> useVersion(arrowVersion)
                    "org.flywaydb" -> useVersion(flywayVersion)
                    "com.fasterxml.jackson.module" -> useVersion(jacksonVersion)
                    "org.postgresql" -> useVersion(postgreVersion)

                    // Test
                    "io.strikt" -> useVersion(striktVersion)
                    "org.junit.jupiter" -> useVersion(junitVersion)
                    "org.testcontainers" -> useVersion(testContainersVersion)
                    "com.github.database-rider" -> useVersion(dbRiderVersion)
                }

                  when(requested.name) {
                      "spring-data-r2dbc" -> useVersion(springDataR2dbcVersion)
                      "r2dbc-postgresql" -> useVersion(r2dbcPostgreVersion)
                      "reactor-core" -> useVersion(reactorVersion)
                      "reactor-kotlin-extensions" -> useVersion(reactorKotlinExtensionsVersion)
                  }
            }
        }
    }
}