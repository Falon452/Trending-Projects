plugins {
    kotlin("android")
    id("com.android.library")
    alias(libs.plugins.compose.compiler)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toInt())
    }
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
    }

    buildFeatures {
        compose = true
    }
}
