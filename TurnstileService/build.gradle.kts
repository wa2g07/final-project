import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "it.polito.wa2.g07"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_16

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    implementation ("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.11.2")
    // Uncomment the next line if you want to use RSASSA-PSS (PS256, PS384, PS512) algorithms:
    //'org.bouncycastle:bcprov-jdk15on:1.60',
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2" )// or 'io.jsonwebtoken:jjwt-gson:0.11.2' for gson
    implementation("org.springframework.boot:spring-boot-starter-security")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "16"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
