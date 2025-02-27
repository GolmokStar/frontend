import java.util.Properties
import java.io.File

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.example.golmokstar"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.golmokstar"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ✅ local.properties에서 API Key 및 Base URL 로드
        val propertiesFile = rootProject.file("local.properties")
        val properties = Properties().apply {
            if (propertiesFile.exists()) load(propertiesFile.inputStream())
        }

        val baseUrl = properties.getProperty("base.url", "https://fallback-url.com/")
        val googleClientId = properties.getProperty("google.client.id", "")

        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$googleClientId\"")
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

configurations.all {
    resolutionStrategy.force("androidx.appcompat:appcompat:1.6.1")
}

dependencies {
    // ✅ AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // ✅ Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // ✅ Navigation
    implementation(libs.androidx.navigation.compose)

    // ✅ Google 로그인
    implementation(libs.google.auth)
    implementation(libs.google.identity)

    // ✅ Retrofit + OkHttp (서버 통신)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)

    // ✅ Coroutines (비동기 처리)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.play.services)

    // ✅ Hilt (의존성 주입)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // ✅ DataStore
    implementation(libs.datastore.preferences)

    // ✅ 권한 요청 라이브러리
    implementation(libs.accompanist.permissions)

    // ✅ Google Maps SDK
    implementation(libs.google.maps)
    implementation(libs.google.maps.utils)
    implementation(libs.google.play.services.location)
    implementation(libs.maps.compose)

    // ✅ JavaPoet (Hilt 관련 빌드 문제 해결용)
    implementation("com.squareup:javapoet:1.13.0")

    // ✅ 테스트 관련 의존성
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // ✅ 디버깅 관련 도구
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("io.coil-kt:coil-compose:2.2.2")

    // Places API
    implementation("com.google.android.libraries.places:places:4.1.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation ("androidx.compose.runtime:runtime-livedata:1.1.0") // LiveData와 Compose 연동을 위한 의존성 추가


}
