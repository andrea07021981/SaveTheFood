// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Dependencies.buildTools)
        classpath(Dependencies.gradlePlugin)
        classpath(Dependencies.serialization)
        classpath(Dependencies.sqlDelightPlugin)
        classpath(Dependencies.navigationPlugin)
        classpath(Dependencies.googleServices)
        //classpath(Dependencies.crashlythics)
        classpath(Dependencies.hilt)
        classpath("com.android.tools.build:gradle:4.1.3")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

repositories {
    mavenCentral()
}

allprojects {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
