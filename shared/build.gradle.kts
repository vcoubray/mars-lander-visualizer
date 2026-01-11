plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotest)
}

kotlin {
    jvm()
//
//    js {
//        browser()
//    }

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

