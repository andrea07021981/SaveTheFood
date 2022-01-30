import java.util.regex.Matcher
import java.util.regex.Pattern

// TODO remove all dependecies that are used in kmm module (retrofit, etc)
plugins {
    id(Plugins.androidApp)
    kotlin(Plugins.kotlin)
    kotlin(Plugins.extensions)
    kotlin(Plugins.kapt)
    id(Plugins.safeArgs)
    id(Plugins.services)
    //id(Plugins.crashlythics)
    id(Plugins.hilt)
    id("kotlin-android")
}

configurations {
    all {
        resolutionStrategy {
            exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-debug")
        }
    }
}
android {
    compileSdk = Application.compileSdk
    buildToolsVersion = Application.buildTools
    defaultConfig {
        applicationId = Application.appId
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk
        versionCode  = Application.versionCode
        versionName = Application.versionName
        testInstrumentationRunner = "com.example.savethefood.CustomTestRunner"
        buildConfigField("String", "BASE_FOOD_URL", "\"https://api.spoonacular.com/\"")
        vectorDrawables {
            useSupportLibrary = true
        }

        //Export DB Schema
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
        buildFeatures {
            compose = true
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
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
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

    // TODO Temporary workaround waiting https://issuetracker.google.com/issues/169249668
    lint {
        disable("NullSafeMutableLiveData")
        isAbortOnError = false
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

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
        kotlinCompilerVersion = Versions.kotlin
    }

    // Enable parcellable
    androidExtensions {
        isExperimental = true
    }
}

fun getCurrentVersionSuffix(): String {
    return when (getCurrentFlavor()) {
        "prod" -> {
            "-prod"
        }
        "uat" -> {
            "-uat"
        }
        "dev" -> {
            "-dev"
        }
        else -> {
            "-dev"
        }
    }
}

fun getCurrentFlavor(): String {
    val taskRequestName = gradle.startParameter.taskRequests.toString()

    var pattern: Pattern = if (taskRequestName.contains("assemble"))
        Pattern.compile("assemble(\\w+)(Release|Debug)")
    else
        Pattern.compile("generate(\\w+)(Release|Debug)")

    val matcher: Matcher = pattern.matcher(taskRequestName)

    return if (matcher.find()) {
        matcher.group(1).toLowerCase()
    } else {
        ""
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
    implementation(Libs.lifecycleRuntime)

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

    // Moshi (Allow to avoit using Call and Response in retrofit, it maps to dataclass directly)
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

    implementation(Libs.koinAndroid)
    implementation(Libs.koinCompose)

    // Compose
    implementation(Compose.runtime)
    implementation(Compose.runtimeLiveData)
    implementation(Compose.foundation)
    implementation(Compose.layout)
    implementation(Compose.ui)
    implementation(Compose.uiUtil)
    implementation(Compose.material)
    implementation(Compose.animation)
    implementation(Compose.iconsExtended)
    implementation(Compose.tooling)
    implementation(Compose.activityCompose)
    implementation(Compose.viewModelCompose)
    implementation(Compose.navigationCompose)
    implementation(Compose.navigationCommon)
    implementation(Compose.constraintCompose)
    implementation(Compose.coilCompose)
    implementation(Compose.uiTest)
    implementation(Compose.splashScreen)
    implementation(Accompanist.insets)
    implementation(Accompanist.systemuicontroller)
    implementation(Accompanist.flowlayouts)
    implementation(Accompanist.placeHolder)

    // KMM module
    implementation(project(path = ":shared"))
}
