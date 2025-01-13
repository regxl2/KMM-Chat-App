import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "1.9.0"
    id("co.touchlab.skie") version "0.9.3"
}

skie {
    features {
        enableSwiftUIObservingPreview = true
    }
    features {
        enableFlowCombineConvertorPreview = true
    }
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true

//            export(libs.mvvm.core)
//            export(libs.mvvm.flow)
        }
    }

    sourceSets {
        androidMain.dependencies {
//            api(libs.mvvm.core)
//            api(libs.mvvm.flow)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {

//            implementation(libs.mvvm.core)
//            implementation(libs.mvvm.flow)
            // put your Multiplatform dependencies here
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.datastore.preferences.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation (libs.ktor.client.content.negotiation)
            implementation(libs.kermit)
        }
        iosMain.dependencies {
//            api(libs.mvvm.core)
//            api(libs.mvvm.flow)

            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "org.example.project.kmmchat.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
