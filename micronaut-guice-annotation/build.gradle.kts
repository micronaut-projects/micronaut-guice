plugins {
    id("io.micronaut.build.internal.guice-module")
}

dependencies {
    api(libs.managed.guice) {
        exclude(group="com.google.guava", module = "guava")
    }
}
