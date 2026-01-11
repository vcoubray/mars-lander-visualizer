plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotest)
    application
}

group = "fr.vco.genetic.algorithm.visualizer"
version = "2.0.0-SNAPSHOT"
application {
    mainClass.set("fr.vco.genetic.algorithm.visualizer.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverCors)
    implementation(libs.ktor.serverContentNegociation)
    implementation(libs.ktor.serverCompression)
    implementation(libs.ktor.serverStatusPages)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serializationKotlinxJson)
    implementation(libs.koin.ktor)
    implementation(libs.koin.loggerSlf4j)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotest.framework.engine)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.assertions.ktor)

}

tasks.named("buildOpenApi") {
    enabled = false
}