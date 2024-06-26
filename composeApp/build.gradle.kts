import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
//    alias(libs.plugins.compose.compiler)

    kotlin("plugin.serialization") version "2.0.0"
//    id("com.google.gms.google-services") version "4.3.4" apply false
   // id("com.google.gms.google-services")

    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            //            export(project("io.github.mirzemehdi:kmpnotifier:0.6.0"))
            baseName = "ComposeApp"
            isStatic = true
            export(libs.kmpNotifier) // пока убрал


            //          export(project("io.github.mirzemehdi:kmpnotifier:0.6.0"))
            // export("io.github.mirzemehdi:kmpnotifier:1.0.0")
//            export("dev.icerock.moko:resources:0.23.0")
//            export("dev.icerock.moko:graphics:0.9.0")
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)

            // отключил для теста на работу koin
//            implementation(libs.koin.android)
//            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.cio)

//            implementation("net.thauvin.erik.urlencoder:urlencoder-lib:1.5.0")

            //Firebase
            api(project.dependencies.platform(libs.firebase.bom))
            api(libs.firebase.analytics)
            api(libs.firebase.crashlytics)



            api(libs.kmpNotifier)
        }
        commonMain.dependencies {
//            implementation(compose.runtime) // до ветки
//            implementation(compose.foundation) // до ветки
            implementation(compose.material3)
            // implementation(compose.ui) // до ветки
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            //https://github.com/Tlaster/PreCompose/blob/master/docs/setup.md
            //  api(compose.foundation) // до ветки
            //    api(compose.animation) // до ветки
//            api(libs.precompose)
//            // api(libs.precompose.molecule) // For Molecule intergration
//            api(libs.precompose.viewmodel) // For ViewModel intergration
            // api(libs.precompose.koin) // For Koin intergration
            //implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha01")
//            implementation("androidx.navigation:navigation-compose:2.8.0-beta01")
            implementation(libs.kotlinx.serialization.json)
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")


//            ////////////////////////
//            // CompositionLocal LocalLifecycleOwner not present
//            implementation("androidx.compose.ui:ui:1.6.7")
//            implementation("androidx.compose.runtime:runtime:1.6.7")
//            implementation("androidx.compose.foundation:foundation:1.6.7")
//            implementation("androidx.compose.animation:animation:1.6.7")
//            implementation("androidx.compose.material:material:1.6.7")

//            implementation("androidx.compose.ui:ui:1.7.0-beta01")
//            implementation("androidx.compose.runtime:runtime:1.7.0-beta01")
//            implementation("androidx.compose.foundation:foundation:1.7.0-beta01")
//            implementation("androidx.compose.animation:animation:1.7.0-beta01")
//            implementation("androidx.compose.material:material:1.7.0-beta01")
//            /////////////////////////

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)

            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)

            //implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.serialization)


            implementation(libs.kermit.v203) //Add latest version

            // отключил для теста на работу koin
            implementation(libs.koin.core)

            implementation(libs.koin.compose) // в этом проблема

//            implementation("org.jetbrains.compose.annotation-internal:annotation:1.6.2")
            //implementation(libs.koin.android)
            //implementation(libs.koin.androidx.compose)

            //api("dev.icerock.moko:resources:0.23.0")

            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

            // implementation(libs.kotlinx.coroutines.core)
            implementation("media.kamel:kamel-image:0.9.4")


            //implementation("org.jetbrains.kotlin:kotlin-serialization:2.0.0-RC1")

            implementation("net.thauvin.erik.urlencoder:urlencoder-lib:1.5.0")
            //implementation("io.github.kevinnzou:compose-webview:0.33.6")

            implementation("io.github.kevinnzou:compose-webview-multiplatform:1.9.2")

//            implementation("com.google.accompanist:accompanist-webview:0.34.0")
            implementation("com.google.accompanist:accompanist-permissions:0.35.1-alpha")

//            implementation("dev.icerock.moko:resources:0.23.0")
            //implementation("org.osmdroid:osmdroid-android:6.1.11")


            // это перепроверить надо ли это
            implementation("org.osmdroid:osmdroid-android:6.1.16")
//            implementation("tech.utsmankece:osm-android-compose:0.0.3")
//            implementation("com.google.maps.android:maps-compose:4.4.0")
//            implementation("com.google.android.gms:play-services-maps:18.2.0")
            ///////////


            //  implementation("androidx.datastore:datastore-core-jvm:1.1.1")

            implementation(libs.androidx.data.store.core)
            implementation(libs.androidx.datastore.core)
            // https://otsembo.hashnode.dev/jetpack-datastore-a-multiplatform-solution
            implementation("org.jetbrains.kotlinx:atomicfu:0.23.2")


            // implementation(libs.androidx.lifecycle.viewmodel.compose)


            //
            //  implementation("com.squareup.okio:okio:3.9.0")

            implementation("com.google.accompanist:accompanist-swiperefresh:0.34.0")
// https://mvnrepository.com/artifact/io.github.mirzemehdi/kmpnotifier
            //implementation("io.github.mirzemehdi:kmpnotifier:0.6.0")
//            implementation("com.google.gms.google-services:4.4.2")
            //api("io.github.mirzemehdi:kmpnotifier:0.6.0")
           // api("io.github.mirzemehdi:kmpnotifier:1.0.0")


            api(libs.kmpNotifier)


        }

        iosMain {
            dependencies {
                // implementation("io.ktor:ktor-client-ios:2.3.10")
                implementation(libs.ktor.client.darwin)
                // implementation("io.github.mirzemehdi:kmpnotifier:0.6.0")
                //api("io.github.mirzemehdi:kmpnotifier:1.0.0")

//                implementation("dev.gitlive:firebase-messaging:1.12.0")
//                implementation("dev.gitlive:firebase-installations:1.12.0")
                api(libs.kmpNotifier)
            }
            //sourceSets["main"].resources.srcDirs("src/commonMain/resources")

            //resources.srcDirs("src/commonMain/resources","src/iosMain/resources")
//            resources.srcDirs("src/commonMain/resources","src/iosMain/resources")


        }
        task("testClasses")
    }
}



android {
    namespace = "net.baza.bazanetclientapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "net.baza.bazanetclientapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }

    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
        // debugImplementation(libs.compose.ui.tooling)

        // не было
        implementation("androidx.compose.ui:ui:1.7.0-alpha08")
        implementation("androidx.compose.runtime:runtime:1.7.0-alpha08")
        implementation("androidx.compose.foundation:foundation:1.7.0-alpha08")
        implementation("androidx.compose.animation:animation:1.7.0-alpha08")
        implementation("androidx.compose.material:material:1.7.0-alpha08")
    }
}

dependencies {
    implementation(libs.androidx.core) // корутины
    //implementation(libs.androidx.core.ktx)

    // отключил для теста на работу koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.compose)

    // отключил для теста на работу koin
    implementation(libs.koin.core)

    // implementation("io.insert-koin:koin-annotations:1.3.0")
    // ksp("io.insert-koin:koin-ksp-compiler:1.3.0")

    implementation("org.jetbrains.compose.annotation-internal:annotation:1.6.2")
    implementation(libs.androidx.constraintlayout)


//    implementation(libs.kotlinx.coroutines.core)
//    implementation("io.github.kevinnzou:compose-webview-multiplatform:1.9.2")


    implementation("org.osmdroid:osmdroid-android:6.1.16")
    // implementation("tech.utsmankece:osm-android-compose:0.0.3")
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime)

//    implementation("androidx.compose.runtime:runtime:1.7.0-beta01")


    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
//    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.compose.ui.tooling.preview)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    //debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
//    implementation(libs.firebase.crashlytics.buildtools)
    // implementation(libs.androidx.compose.material)


    // красивый переход от Лакнера
    // https://www.youtube.com/watch?v=mE5bLb42_Os
    //implementation("androidx.compose.animation:animation:1.7.0-alpha07")


    implementation(libs.androidx.data.store.core)
    implementation(libs.androidx.datastore.core)
    // https://otsembo.hashnode.dev/jetpack-datastore-a-multiplatform-solution


    ////////////////////////
    // CompositionLocal LocalLifecycleOwner not present
//    implementation("androidx.compose.ui:ui:1.6.7")
//    implementation("androidx.compose.runtime:runtime:1.6.7")
//    implementation("androidx.compose.foundation:foundation:1.6.7")
//    implementation("androidx.compose.animation:animation:1.6.7")
//    implementation("androidx.compose.material:material:1.6.7")




//    implementation("androidx.compose.ui:ui:1.7.0-alpha05")
//    implementation("androidx.compose.runtime:runtime:1.7.0-alpha05")
//    implementation("androidx.compose.foundation:foundation:1.7.0-alpha05")
//    implementation("androidx.compose.animation:animation:1.7.0-alpha05")
//    implementation("androidx.compose.material:material:1.7.0-alpha05")

//    implementation("androidx.compose.ui:ui:1.7.0-beta01")
//    implementation("androidx.compose.runtime:runtime:1.7.0-beta01")
//    implementation("androidx.compose.foundation:foundation:1.7.0-beta01")
//    implementation("androidx.compose.animation:animation:1.7.0-beta01")
//    implementation("androidx.compose.material:material:1.7.0-beta01")

    //         // не было
    implementation("androidx.compose.ui:ui:1.7.0-alpha08")
    implementation("androidx.compose.runtime:runtime:1.7.0-alpha08")
    implementation("androidx.compose.foundation:foundation:1.7.0-alpha08")
    implementation("androidx.compose.animation:animation:1.7.0-alpha08")
    implementation("androidx.compose.material:material:1.7.0-alpha08")
    debugImplementation(libs.compose.ui.tooling)

    /////////////////////////

    implementation(libs.firebase.perf.ktx)



}


