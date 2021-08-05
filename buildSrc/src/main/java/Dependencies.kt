

object Dependencies {
    const val buildTools = "com.android.tools.build:gradle:${Versions.gradle}"
    const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val serialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    const val sqlDelightPlugin = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"
    const val navigationPlugin = "android.arch.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationArgs}"
    const val googleServices = "com.google.gms:google-services:${Versions.play}"
    const val crashlythics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlythics}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
}