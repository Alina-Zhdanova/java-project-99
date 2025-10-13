plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
//	checkstyle
	id("org.sonarqube") version "6.3.1.5724"
	jacoco
	application
	id("io.freefair.lombok") version "8.13.1"
	id("io.sentry.jvm.gradle") version "5.12.0"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

application {
	mainClass.set("hexlet.code.AppApplication")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("net.datafaker:datafaker:2.0.1")
	implementation("org.instancio:instancio-junit:3.3.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")

	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	annotationProcessor("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.jacocoTestReport { reports { xml.required.set(true) } }

tasks.withType<Test> {
	useJUnitPlatform()
}

sonar {
	properties {
		property("sonar.projectKey", "Alina-Zhdanova_java-project-99")
		property("sonar.organization", "alina-zhdanova")
	}
}

tasks.sentryBundleSourcesJava {
	enabled = System.getenv("SENTRY_AUTH_TOKEN") != null
}

sentry {
	includeSourceContext.set(true)

	org.set("alina-zhdanova")
	projectName.set("java-spring-boot")
	authToken.set(System.getenv("SENTRY_AUTH_TOKEN"))
}
