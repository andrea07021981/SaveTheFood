import java.util.regex.Matcher
import java.util.regex.Pattern

plugins {
    id(Plugins.androidApp)
    kotlin(Plugins.kotlin)
    kotlin(Plugins.extentions)
    kotlin(Plugins.kapt)
    id(Plugins.safeArgs)
    id(Plugins.services)
    //id(Plugins.crashlythics)
    id(Plugins.hilt)
}

configurations {
    all {
        resolutionStrategy {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-debug")
        }
    }
}
android {
    compileSdkVersion(Application.compileSdk)
    buildToolsVersion(Application.buildTools)
    defaultConfig {
        applicationId = Application.appId
        minSdkVersion(Application.minSdk)
        targetSdkVersion(Application.targetSdk)
        versionCode  = Application.versionCode
        versionName = Application.versionName
        testInstrumentationRunner = "com.example.savethefood.CustomTestRunner"
        buildConfigField("String", "BASE_FOOD_URL", "\"https://api.spoonacular.com/\"")

        //Export DB Schema
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
        buildFeatures {
            viewBinding = true // View binding is a very much simpler version/a subset of data binding.
            //The difference between the two is that view binding is only for view
            // references and not for binding UI with data sources
            dataBinding = true
        }
        packagingOptions {
            exclude("**/attach_hotspot_windows.dll")
            exclude("META-INF/licenses/**")
            exclude("META-INF/AL2.0")
            exclude("META-INF/LGPL2.1")
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }

        // Always show the result of every unit test when running via command line, even if it passes.
        testOptions.unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true //It's mandatory to not break unit tests
            // ...
        }

        //Shared fake data resource for test and android test
        sourceSets {
            getByName("androidTest").java.srcDirs("src/sharedtest/java")
            getByName("test").java.srcDirs("src/sharedtest/java")
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("/Users/andreafranco/.android/debug.keystore")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
    }


    //For migration tests
    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = getCurrentVersionSuffix()
            isMinifyEnabled = false
            isTestCoverageEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            testProguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguardTest-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isMinifyEnabled = true
            versionNameSuffix = getCurrentVersionSuffix()
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

    }

    flavorDimensions("server")

    productFlavors {
        create("prod") {
            dimension = "server"
            buildConfigField("String", "BASE_URL", "\"http://production.com/api/\"")
            resValue("string", "app_name", "Save The Food")
        }
        create("uat") {
            applicationIdSuffix = ".uat"
            dimension = "server"
            buildConfigField("String", "BASE_URL", "\"http://qa.com/api/\"")
            resValue("string", "app_name", "Save The Food Uat")
        }
        create("dev") {
            applicationIdSuffix = ".dev"
            dimension = "server"
            buildConfigField("String", "BASE_URL", "\"http://dev.com/api/\"")
            resValue("string", "app_name", "Save The Food Dev")
        }
    }

    // Enable parcellable
    androidExtensions {
        isExperimental = true
    }
}

fun getCurrentVersionSuffix(): String {
    val currentFlavor = getCurrentFlavor()
    if (currentFlavor == "prod") {
        return "-prod"
    } else if (currentFlavor == "uat") {
        return "-uat"
    } else if (currentFlavor == "dev") {
        return "-dev"
    } else {
        return "-dev"
    }
}

fun getCurrentFlavor(): String {
    val taskRequestName = getGradle().getStartParameter().getTaskRequests().toString()

    var pattern: Pattern

    if (taskRequestName.contains("assemble"))
        pattern = Pattern.compile("assemble(\\w+)(Release|Debug)")
    else
        pattern = Pattern.compile("generate(\\w+)(Release|Debug)")

    val matcher: Matcher = pattern.matcher(taskRequestName)

    if (matcher.find()) {
        return matcher.group(1).toLowerCase()
    } else {
        return ""
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.kotlinStd)
    implementation(Libs.material)
    // Support libraries
    implementation(Libs.appCompat)
    implementation(Libs.fragment)
    implementation(Libs.constraintLayout)
    implementation(Libs.serviceBasement)

    // Android KTX
    implementation(Libs.coreKtx)

    // Room and Lifecycle dependencies
    implementation(Libs.roomRuntime)
    implementation(Libs.palette)
    implementation(Libs.recycleViewSelection)
    kapt(Libs.roomCompiler)
    implementation(Libs.room)
    implementation(Libs.maps)
    implementation(Libs.mapUtils)
    implementation(Libs.serviceLocation)
    implementation(Libs.lifecycleExt)
    implementation(Libs.liveData)

    // Coroutines
    implementation(Libs.coroutineCore)
    implementation(Libs.coroutineAndroid)

    // Navigation
    implementation(Libs.navigationFragment)
    implementation(Libs.navigationUi)

    //Cards
    implementation(Libs.cardView)

    implementation(Libs.multiDex)
    implementation(Libs.zXing)

    // Viewpager2
    implementation(Libs.viewPager)

    // Moshi
    implementation(Libs.moshi)
    implementation(Libs.moshiKotlin)

    // Retrofit with Moshi Converter
    implementation(Libs.retrofit)
    implementation(Libs.moshiConverter)

    // Retrofit Coroutines Support
    implementation(Libs.retrofitCoroutineAdapter)

    // Glide
    implementation(Libs.glide)

    // RecyclerView
    implementation(Libs.recycleView)

    // Paging library
    implementation(Libs.paging)

    //Interceptor and http loggin
    implementation(Libs.okHttp)
    implementation(Libs.interceptor)

    // Testing
    // Espresso
    // Core library
    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation(Libs.runner)
    androidTestImplementation(Libs.rules)
    implementation(Libs.coreTesting)

    // AndroidX Test - Instrumented testing
    androidTestImplementation(Libs.testX)
    androidTestImplementation(Libs.testX)
    androidTestImplementation(Libs.espressoCore)
    androidTestImplementation(Libs.espressoContrib)
    testImplementation(Libs.hamcrest)
    testImplementation(Libs.coroutineeTest)
    // Dependencies for Android instrumented unit tests
    androidTestImplementation(Libs.jUnit)
    androidTestImplementation(Libs.coroutineeTest)
    androidTestImplementation(Libs.coreTesting)

    // AndroidX Test - JVM testing
    testImplementation(Libs.coreKtx)
    testImplementation(Libs.robolectric)
    testImplementation(Libs.jUnitX)

    // Testing code should not be included in the main code.
    // Once https://issuetracker.google.com/128612536 is fixed this can be fixed.
    debugImplementation(Libs.fragmentTesting)
    implementation(Libs.androidTestCore)

    // Dependencies for Android instrumented unit tests
    testImplementation(Libs.mockito)
    androidTestImplementation(Libs.mockitoDex)
    androidTestImplementation(Libs.espressoContrib)

    //Test Espresso idling resources
    implementation(Libs.idlingRes)

    //Custom login effect github
    implementation(Libs.loadingButton)

    //Analytics
    implementation(Libs.analyticsKtx)
    implementation(Libs.analytics)

    implementation(Libs.workRuntime)

    //Firebase Auth
    implementation(Libs.firebaseAuth)

    //Firebase crashlytics
    //implementation(Libs.crashLytics)

    // Base Hilt dependencies:
    implementation(Libs.androidHilt)
    kapt(Libs.androidHiltCompiler)

    // ViewModel support dependencies:
    implementation(Libs.lifeCycleVm)
    kapt(Libs.hiltCompiler)

    implementation(Libs.hiltWork)
    // When using Java.
    annotationProcessor(Libs.hiltCompiler)

    // For Robolectric tests.
    testImplementation(Libs.hiltAndroidTesting)
    // ...with Kotlin.
    kaptTest(Libs.androidHiltCompiler)
    // ...with Java.
    testAnnotationProcessor(Libs.androidHiltCompiler)

    // For instrumented tests.
    androidTestImplementation(Libs.hiltAndroidTesting)
    // ...with Kotlin.
    kaptAndroidTest(Libs.androidHiltCompiler)
    // ...with Java.
    androidTestAnnotationProcessor(Libs.androidHiltCompiler)

    // MockWebServer
    testImplementation(Libs.mockWebserver)
    testImplementation(Libs.httpIdlingRes)

    testImplementation(Libs.mavenAntTask)

    implementation(Libs.stepper)

    implementation(Libs.shimmer)

    // KMM module
    implementation(project(":shared"))
}
