plugins {
    id("io.micronaut.build.internal.guice-module")
}

dependencies {
    implementation(libs.managed.guice) {
        exclude(group="com.google.guava", module = "guava")
    }
}
