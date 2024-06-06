plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false


    alias(libs.plugins.jetbrainsKotlinAndroid) apply false

//    id("com.google.gms.google-services")
    //id("com.google.gms.google-services") version "4.4.2" apply false
//    id("com.google.gms.google-services") apply false

    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}