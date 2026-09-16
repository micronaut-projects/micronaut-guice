plugins {
    id("io.micronaut.build.internal.kotlin-kapt")
}

description = "Test Suite for testing and documenting the Guice integration"

repositories {
    mavenCentral()
}

dependencies {
    // KAPT: the Guice import visitor adds associated bean definitions, which KSP class elements do not support
    kaptTest(mn.micronaut.inject.java)
    kaptTest(projects.micronautGuiceProcessor)

    testImplementation(projects.micronautGuice)
    testImplementation(mn.micronaut.runtime)
    testImplementation(mnTest.micronaut.test.junit5)

    testRuntimeOnly(mnLogging.logback.classic)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testRuntimeOnly(mnTest.junit.platform.suite)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
