plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.recipeapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.recipeapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    /*************** IMPORTANT FIX (Java 21 + Kotlin Toolchain) *****************/
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        jvmToolchain(21)
    }
    /****************************************************************************/

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    /** Android basics */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.core:core-splashscreen:1.0.1")

    /** Glide + UI */
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    /** Firebase */
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    /** Google Play Services */
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    /** Coroutines */
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation(libs.firebase.crashlytics.buildtools)

    /** Testing */
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /** RoomDB - UPDATED VERSIONS */
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")  // FIXED: Use kapt, not implementation

    /** LiveData */
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.runtime:runtime-livedata")

    /** FIX: Force single version of JetBrains annotations */
    implementation("org.jetbrains:annotations:23.0.0")
}

/** FIX: Exclude duplicate annotations library */
configurations.all {
    exclude(group = "com.intellij", module = "annotations")
}