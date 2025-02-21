plugins {
    kotlin("android")
    id("com.android.library")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt.gradle.plugin)
    kotlin("kapt")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toInt())
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "com.falon.theme"

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    dependencies {
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.material3)
        implementation(libs.androidx.google.fonts)
        implementation(libs.hilt.android)
        implementation(libs.hilt.navigation.compose)
        kapt(libs.hilt.compiler)
        implementation(libs.datastore.preferences)
        implementation(libs.androidx.core.ktx)
        testImplementation(libs.junit5.api)
        testImplementation(libs.junit5.params)
        testRuntimeOnly(libs.junit5.engine)
        testImplementation(libs.mockk)
        testImplementation(libs.coroutines.test)
        testImplementation(libs.kotlin.test)
    }

    buildFeatures {
        compose = true
    }
}
