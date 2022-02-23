plugins {
    `java-library`
    id("application")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}


dependencies {
    val edcGroup = "org.eclipse.dataspaceconnector"
    val edcVersion = "0.0.1-SNAPSHOT"

    api("$edcGroup:core:$edcVersion")
    api("$edcGroup:filesystem-configuration:$edcVersion")
    api("$edcGroup:assetindex-memory:$edcVersion")
    api("$edcGroup:transfer-process-store-memory:$edcVersion")
    api("$edcGroup:contractnegotiation-store-memory:$edcVersion")
    api("$edcGroup:contractdefinition-store-memory:$edcVersion")
}

application {
    mainClass.set("org.eclipse.dataspaceconnector.boot.system.runtime.BaseRuntime")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    exclude("**/pom.properties", "**/pom.xm")
    mergeServiceFiles()
    archiveFileName.set("edc.jar")
}
