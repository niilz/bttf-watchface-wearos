plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "de.niilz.wearos.watchface.bttf"
    compileSdk = 34

    defaultConfig {
        applicationId = "de.niilz.wearos.watchface.bttf"
        minSdk = 30
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

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

    // Compose
    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_19.majorVersion
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")

    implementation("androidx.wear.watchface:watchface:1.1.1")
    implementation("androidx.wear.watchface:watchface-complications-data-source-ktx:1.1.1")
    implementation("androidx.wear.watchface:watchface-editor:1.1.1")
    // Optional (for custom complication rendering)
    implementation("androidx.wear.watchface:watchface-complications-rendering:1.1.1")

  // Compose
    val composeBom = platform("androidx.compose:compose-bom:2023.06.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui-unit-android:1.5.0")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Health
    implementation("androidx.health:health-services-client:1.0.0-rc01")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-core:1.0.0")

    // Test
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}
