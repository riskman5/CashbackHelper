plugins {
    id("java")
}

group = "ru.babenko"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":controller"))
    implementation(project(":service"))
    implementation(project(":dal"))
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.5")
    testImplementation("com.h2database:h2:2.1.212")
    testImplementation("org.mockito:mockito-core:4.2.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}