plugins {
    id("io.micronaut.build.internal.guice-module")
}

dependencies {
    implementation(projects.micronautGuiceAnnotation)
    implementation(mn.micronaut.core.processor)
    implementation(libs.managed.guice) {
        exclude(group="com.google.guava", module = "guava")
    }
    testImplementation(mn.micronaut.inject.java.test)
    testImplementation(projects.micronautGuice)
}
