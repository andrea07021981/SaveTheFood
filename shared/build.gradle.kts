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

    /*tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }*/

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
                implementation(Compose.runtime)
                implementation(Compose.runtimeLiveData)
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
                // we need to tell the compiler where to look for generated Kotlin Native classes. It could be added to commonMain as well, but we only need those classes in iOS binary.
                //kotlin.srcDir("${buildDir.absolutePath}/generated/source/kaptKotlin/")
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
    compileSdkVersion(Application.compileSdk)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(Application.minSdk)
        targetSdkVersion(Application.targetSdk)
        //versionCode = 1
        //versionName = "1.0"
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    // Always show the result of every unit test when running via command line, even if it passes.
    testOptions.unitTests {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true //It's mandatory to not break unit tests
        // ...
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