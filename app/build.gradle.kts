plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
    id("kotlin-kapt") // enable kapt for dataBinding and for hilt
    id("com.google.dagger.hilt.android") //hilt
}

android {
    namespace = "com.miassolutions.rentatool"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.miassolutions.rentatool"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5") // Latest stable version as of December 11, 2024 :contentReference[oaicite:1]{index=1}
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5") // Latest stable version as of December 11, 2024 :contentReference[oaicite:2]{index=2}

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7") // Latest stable version as of December 11, 2024 :contentReference[oaicite:3]{index=3}
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7") // Latest stable version as of December 11, 2024 :contentReference[oaicite:4]{index=4}

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1") // Assumed latest stable version
    implementation("androidx.room:room-ktx:2.6.1") // Assumed latest stable version
    ksp("androidx.room:room-compiler:2.6.1") // Assumed latest stable version

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0") // Assumed latest stable version
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Assumed latest stable version
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0") // Assumed latest stable version

    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0") // Assumed latest stable version
    ksp("com.github.bumptech.glide:compiler:4.16.0")

//    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.0") // Assumed latest stable version

//    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Assumed latest stable version


    //hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

}