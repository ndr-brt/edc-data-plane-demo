plugins {
    `java-library`
    id("application")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    api("org.eclipse.dataspaceconnector:web-spi:0.0.1-SNAPSHOT")
    api("org.eclipse.dataspaceconnector:core-base:0.0.1-SNAPSHOT")
    api("org.eclipse.dataspaceconnector:core-boot:0.0.1-SNAPSHOT")
    api("org.eclipse.dataspaceconnector:http:0.0.1-SNAPSHOT")
    api("org.eclipse.dataspaceconnector:data-plane-spi:0.0.1-SNAPSHOT")
    api("org.eclipse.dataspaceconnector:data-plane-framework:0.0.1-SNAPSHOT")
    api("org.eclipse.dataspaceconnector:data-plane-http:0.0.1-SNAPSHOT")
    api("org.eclipse.dataspaceconnector:data-plane-api:0.0.1-SNAPSHOT")
}

application {
    mainClass.set("org.eclipse.dataspaceconnector.boot.system.runtime.BaseRuntime")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    exclude("**/pom.properties", "**/pom.xm", "jndi.properties", "jetty-dir.css", "META-INF/maven/**")
    mergeServiceFiles()
    archiveFileName.set("data-plane-server.jar")
}