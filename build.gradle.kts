// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.

/**
 * configuration section is for gradle itself (i.e. changes to how gradle is able to perform the build).
 * So this section will usually include the Android Gradle plugin.
 */
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://www.jetbrains.com/intellij-repository/releases")
        maven(url = "https://jetbrains.bintray.com/intellij-third-party-dependencies")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath(Dependencies.serialization)
        classpath(Dependencies.sqlDelightPlugin)
        classpath(Dependencies.navigationPlugin)
        classpath(Dependencies.googleServices)
        //classpath(Dependencies.crashlythics)
        classpath(Dependencies.hilt)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://www.jetbrains.com/intellij-repository/releases")
        maven(url = "https://jetbrains.bintray.com/intellij-third-party-dependencies")
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()

        if (!Compose.snapshot.isEmpty()) {
            maven(url = "https://androidx.dev/snapshots/builds/${Compose.snapshot}/artifacts/repository/")
        }
        if (Compose.version.endsWith("SNAPSHOT")) {
            maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }

}

/**
 *  for the modules being built by Gradle.
 */
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
