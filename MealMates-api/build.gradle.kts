import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("com.google.cloud.tools.jib") version "3.4.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

group = "com.backend"
version = "1.0.0"

tasks.named<BootBuildImage>("bootBuildImage") {
    builder.set("gcr.io/buildpacks/builder")
    environment.set(environment.get() + mapOf("BP_JVM_VERSION" to "17"))
    imageName.set("gcr.io/mealmates-430307/mealmates-api")
    //set GOOGLE_RUNTIME_VERSION to 21
    environment.set(environment.get() + mapOf("GOOGLE_RUNTIME_VERSION" to "17"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Used to connect to our database
    implementation("org.postgresql:postgresql:42.3.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // for calling REST APIs with Ktor
    implementation("io.ktor:ktor-client-core:2.3.8")
    implementation("io.ktor:ktor-client-android:2.3.8")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    // for deserializing strings to objects
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.8")

    // for parsing JSON
    implementation("com.google.code.gson:gson:2.8.9")
    // for gcloud app engine
    implementation("javax.servlet:javax.servlet-api:3.1.0")
    implementation("com.google.appengine:appengine:+")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
