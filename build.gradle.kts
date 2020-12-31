import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}

group = "com.mthaler"
version = "0.1"

repositories {
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/hotkeytlt/maven") }
}

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:4.3.2")
    testImplementation("io.kotest:kotest-assertions-core:4.3.2")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
