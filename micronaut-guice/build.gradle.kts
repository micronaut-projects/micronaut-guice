plugins {
    id("io.micronaut.build.internal.guice-module")
}

dependencies {
    api(mn.micronaut.context)
    api(projects.micronautGuiceAnnotation)
    api(libs.managed.guice) {
        exclude(group="com.google.guava", module = "guava")
    }
    runtimeOnly(libs.managed.guava)
    testAnnotationProcessor(projects.micronautGuiceProcessor)
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testImplementation(mnTest.mockito.junit.jupiter)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

//tasks {
//    compileTestJava {
//        options.isFork = true
//        options.forkOptions.jvmArgs = listOf("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005")
//    }
//}
