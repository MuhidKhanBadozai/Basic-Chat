plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // 👇 Firebase plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.basics"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.basics"
        minSdk = 28
        targetSdk = 34
        versionCode = 4            // increment integer (previous was 2 for 1.0.1)
        versionName = "1.0.3"       // new semantic version
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // ✅ Signing configuration for release builds
    signingConfigs {
        create("release") {
            storeFile = file("my-release-key.jks")
            storePassword = "123456"     // ⚠️ use your real password
            keyAlias = "release_key"
            keyPassword = "123456"       // ⚠️ same as above
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            // For debug builds, no signing needed
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // --- Compose + AndroidX ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // --- Firebase ---
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.0")

    // --- Navigation ---
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
