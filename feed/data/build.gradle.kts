plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.gradle.plugin)
    kotlin("kapt")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toInt())
    }
}

android {
    namespace = "com.falon.feed.data"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}

dependencies {
    implementation(project(":feed:domain"))
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.hilt.android)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    kapt(libs.hilt.compiler)
}
