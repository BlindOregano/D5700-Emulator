plugins {
    kotlin("jvm") version "1.9.23"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}
application {
    mainClass.set("org.kevinparks.emulator.MainKt")
}

kotlin {
    jvmToolchain(21) // ✅ Use JVM 21 cleanly
}

