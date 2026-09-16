pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("io.micronaut.build.shared.settings") version "8.1.1"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "guice-parent"

include("micronaut-guice-bom")
include("micronaut-guice")
include("micronaut-guice-annotation")
include("micronaut-guice-processor")

include("test-suite")
include("test-suite-groovy")
include("test-suite-kotlin")
include("test-suite-python")

val micronautVersion = providers.gradleProperty("micronautVersion")

configure<io.micronaut.build.MicronautBuildSettingsExtension> {
    useStandardizedProjectNames.set(true)
    importMicronautCatalog()
}
