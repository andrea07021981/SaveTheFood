

object Dependencies {
    val buildTools = "com.android.tools.build:gradle:${Versions.gradle}"
    val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val serialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    val sqlDelightPlugin = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"
    val navigationPlugin = "android.arch.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationArgs}"
    val googleServices = "com.google.gms:google-services:${Versions.play}"
    val crashlythics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlythics}"
    val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
}