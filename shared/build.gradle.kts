import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin(Plugins.multiplatform)
    kotlin(Plugins.serialization)
    id(Plugins.androidLib)
    kotlin(Plugins.extensions)
    id(Plugins.sqlDelight)
}
// TODO create a build folder with all versions for gradle kts

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libs.coroutineCore)
                implementation(Libs.ktorNative)
                implementation(Libs.serialization)
                implementation(Libs.ktorSerialization)
                implementation(Libs.sqlDelightRuntime)
                implementation(Libs.sqlDelightCoroutines)
                implementation(Libs.logBack)
                implementation(Libs.ktorClientLogging)
                // MOKO - MVVM
                implementation(Libs.moko)
                // KOIN for DI
                // Koin for Kotlin Multiplatform
                implementation(Libs.koinNative)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Libs.ktorAndroid)
                implementation(Libs.sqlDelightAndroid)
                implementation(Libs.koinAndroid)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation(KotlinTestLibs.jvm)
                implementation(KotlinTestLibs.junit)
                implementation(AndroidTestLibs.core)
                implementation(AndroidTestLibs.junit)
                implementation(AndroidTestLibs.runner)
                implementation(AndroidTestLibs.rules)
                implementation(Libs.coroutineeTest)
                implementation(Libs.robolectric)
            }
        }
        val iosMain by getting  {
            dependencies {
                implementation(Libs.ktorIos)
                implementation(Libs.sqlDelightNative)
                implementation(Libs.coroutineCore) {
                    version {
                        strictly(Versions.coroutine)
                    }
                }
            }
        }
        val iosTest by getting {
            dependencies {
                implementation(Libs.sqlDelightNative)
                implementation(Libs.coroutineNative)
            }
        }
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(22)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

sqldelight {
    database("SaveTheFoodDatabase") {
        packageName = "com.example.savethefood.shared.cache"
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)