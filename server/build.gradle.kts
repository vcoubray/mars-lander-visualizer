plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    alias(libs.plugins.serialization)
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
    testImplementation(libs.kotlin.testJunit)
}