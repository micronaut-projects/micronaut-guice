plugins {
    id("groovy")
}

description = "Test Suite for testing and documenting the Guice integration"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(mn.micronaut.inject.groovy)
    testImplementation(projects.micronautGuiceProcessor)

    testImplementation(projects.micronautGuice)
    testImplementation(mn.micronaut.runtime)
    testImplementation(mnTest.micronaut.test.spock)

    testRuntimeOnly(mnLogging.logback.classic)
    testRuntimeOnly(mnTest.junit.platform.suite)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
