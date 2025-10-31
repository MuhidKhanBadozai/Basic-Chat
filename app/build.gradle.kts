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
        versionCode = 5              // Increment for each new release
        versionName = "1.5.0"        // Semantic version
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // ✅ Secure signing config using environment variables (with fallback)
    signingConfigs {
        create("release") {
            // Path to your keystore (default: app/my-release-key.jks)
            val keystorePath = System.getenv("ANDROID_KEYSTORE_FILE")
                ?: "app/my-release-key.jks"
            storeFile = file(keystorePath)

            // Use environment vars OR fallback to local test values
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "123456"
            keyAlias = System.getenv("KEY_ALIAS") ?: "release_key"
            keyPassword = System.getenv("KEY_PASSWORD") ?: "123456"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // ✅ Use the secure release signing config
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            // Debug builds skip signing
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
