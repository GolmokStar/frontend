import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
}

val properties = Properties().apply {load(project.rootProject.file("local.properties").inputStream())}

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

        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${project.findProperty("google.client.id")}\"")
        buildConfigField("String", "BASE_URL", "\"${project.findProperty("base.url")}\"")
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

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM을 사용하여 버전 자동 관리
    implementation(platform(libs.androidx.compose.bom))

    // Compose UI 라이브러리
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // 테스트 관련 의존성
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // 디버그용 도구
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // 🔹 Google 로그인
    implementation(libs.google.auth)
    implementation(libs.google.identity)

    // 🔹 Retrofit & OkHttp (서버 통신)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)

    // 🔹 Coroutines (비동기 처리)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.play.services)

    // 🔹 Hilt (의존성 주입)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // 🔹 DataStore
    implementation(libs.datastore.preferences)

    // 🔹 권한 요청 라이브러리
    implementation(libs.accompanist.permissions)

    // ✅ Google Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.1.0")

    // ✅ Maps Utils (마커 클러스터링 및 추가 기능)
    implementation("com.google.maps.android:android-maps-utils:3.4.0")

    // ✅ Google Play Services - 위치 API (Fused Location Provider)
    implementation("com.google.android.gms:play-services-location:21.0.1")


    // ✅ Jetpack Compose용 Google Maps 라이브러리 (필수)
    implementation("com.google.maps.android:maps-compose:2.11.2")


    implementation("com.squareup:javapoet:1.13.0") // ✅ Explicitly force JavaPoet version

}
