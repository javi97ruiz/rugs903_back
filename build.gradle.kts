plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    id("jacoco")
}

group = "dev.javi"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
jacoco {
    toolVersion = "0.8.11" // versión estable actual
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    //implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.session:spring-session-data-redis")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.testcontainers:junit-jupiter")
    //testImplementation("org.testcontainers:mongodb")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // Spring Boot Starter Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // JWT (Java JWT de Auth0)
    implementation("com.auth0:java-jwt:4.4.0")

    // Encriptación de contraseñas
    implementation("org.springframework.security:spring-security-crypto")

    // (Opcional) Para usar anotaciones como @ConfigurationProperties si necesitas
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.security:spring-security-web")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("com.stripe:stripe-java:24.6.0")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("app.jar") // nombre que Render puede usar
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // se asegura de correr los tests antes del reporte

    reports {
        xml.required.set(true) // para CI (opcional)
        html.required.set(true) // para ver en navegador
        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude(

                        "**/models/**", // excluye paquetes model
                        "**/dto/**", // excluye paquetes dto
                        "**/security/**",
                        "**/repositories/**",
                        "**/config/**", // configuración
                        "**/*Application*", // clase main
                        "**/exceptions/**" // excepciones, si tienes
                    )
                }
            })
        )
    }
}
