apply(from = "./resolutions.gradle.kts")
apply(from = "./repositories.gradle.kts")

rootProject.name = "adlershof"

include(
    ":application-feeder",
    ":common",
    ":domain",
    ":persistence"
)