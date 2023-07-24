plugins {
	java
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.romero"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.jetbrains:annotations:24.0.0")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.postgresql:r2dbc-postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("com.squareup.okhttp3:okhttp:4.0.1")
	testImplementation("com.squareup.okhttp3:mockwebserver:4.0.1")
	compileOnly("org.projectlombok:lombok:1.18.28")
	annotationProcessor("org.projectlombok:lombok:1.18.28")
	testCompileOnly("org.projectlombok:lombok:1.18.28")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.28")
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
	implementation("io.lettuce:lettuce-core")
	implementation("com.github.ben-manes.caffeine:caffeine:3.0.4")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
