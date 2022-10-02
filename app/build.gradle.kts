plugins {
    id("com.android.application")
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.hirno.assignment"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    buildTypes {
        getByName("release") {
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
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}

dependencies {
    // Test
    testImplementation("junit:junit:$JUNIT_VERSION")
    testImplementation("org.robolectric:robolectric:4.5.1")
    testImplementation("androidx.test.ext:junit:1.1.3")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$COROUTINES_VERSION") {
        // https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-debug#debug-agent-and-android
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-debug")
    }

    // AndroidTest
    androidTestImplementation("junit:junit:$JUNIT_VERSION")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$KOTLINX_COROUTINES")
    androidTestImplementation("androidx.test.ext:junit:$ANDROIDX_TEST_EXT")
    androidTestImplementation("androidx.test.espresso:espresso-core:$ANDROIDX_TEST_ESPRESSO")
    androidTestImplementation("androidx.test.espresso:espresso-intents:$ANDROIDX_TEST_ESPRESSO")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$ANDROIDX_TEST_ESPRESSO") {
        exclude(module = "protobuf-lite")
    }
    debugImplementation("androidx.fragment:fragment-testing:$ANDROIDX_FRAGMENT")

    // AndroidX
    implementation("androidx.core:core-ktx:$ANDROIDX_CORE")
    implementation("androidx.appcompat:appcompat:$ANDROIDX_APPCOMPAT")
    implementation("com.google.android.material:material:$ANDROIDX_MATERIAL")
    implementation("androidx.constraintlayout:constraintlayout:$ANDROIDX_CONSTRAINT_LAYOUT")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$LIFECYCLE_VERSION")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VERSION")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINES_VERSION")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$COROUTINES_VERSION")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$RETROFIT_VERSION")
    implementation("com.squareup.retrofit2:converter-gson:$RETROFIT_VERSION")
    implementation("com.squareup.okhttp3:okhttp:$OK_HTTP_VERSION")
    implementation("com.squareup.okhttp3:logging-interceptor:$OK_HTTP_VERSION")

    // Room
    implementation("androidx.room:room-runtime:$ROOM_VERSION")
    ksp("androidx.room:room-compiler:$ROOM_VERSION")
    implementation("androidx.room:room-ktx:$ROOM_VERSION")
}