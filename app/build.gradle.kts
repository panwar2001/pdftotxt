plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.relay") version "0.3.11"
}
android {
    namespace = "com.panwar2001.pdf2txt"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.panwar2001.pdf2txt"
        minSdk = 24
        targetSdk = 33
        versionCode = 4
        versionName = "1.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources=true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures{
        //noinspection DataBindingWithoutKapt
        dataBinding=true
        compose = true
    }
 viewBinding {
     enable=true
 }
    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:+")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("com.tom-roush:pdfbox-android:2.0.27.0")
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.1") //pdf preview
    // include that repository in your project's build.gradle file.
    implementation("com.google.android.play:review:2.0.1")
    // For Kotlin users also import the Kotlin extensions library for Play In-App Review:
    implementation("com.google.android.play:review-ktx:2.0.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}