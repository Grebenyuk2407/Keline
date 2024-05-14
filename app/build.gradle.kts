plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("com.google.gms.google-services")

}

android {
    namespace = "dev.androidbroadcast.keline"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.androidbroadcast.keline"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //loading button
    implementation(libs.loading.button.android)

    //Glide
    implementation(libs.glide)

    //circular image
    implementation(libs.circleimageview)

    //viewpager2 indicatior
    implementation(libs.viewpagerindicator)

    //stepView
    implementation(libs.stepview)

    //Android Ktx
    implementation(libs.androidx.navigation.fragment.ktx)

    //Dagger hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //Firebase
    implementation(libs.firebase.auth)

    //Coroutines with firebase
    implementation(libs.kotlinx.coroutines.play.services)
}