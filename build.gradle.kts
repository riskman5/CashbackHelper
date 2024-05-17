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
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}