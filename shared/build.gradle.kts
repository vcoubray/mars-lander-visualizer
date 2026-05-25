@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.serialization)
}

kotlin {
    jvm()

    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation(libs.ktor.clientCore)
            implementation(libs.ktor.serializationKotlinxJson)
        }
        commonTest.dependencies {
           implementation(libs.kotest.framework.engine)
           implementation(libs.kotest.assertions.core)
        }
    }
}

