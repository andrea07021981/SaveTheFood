
// TODO create sub objects like PeopleInSpace app
object Libs {

    const val kotlinStd = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val material = "com.google.android.material:material:${Versions.material}"

    // Support libraries
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val serviceBasement = "com.google.android.gms:play-services-basement:${Versions.playBasement}"

    // Android KTX
    const val coreKtx = "androidx.core:core-ktx:${Versions.core}"

    // Room and Lifecycle dependencies
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val palette = "androidx.palette:palette-ktx:${Versions.palette}"
    const val recycleViewSelection = "androidx.recyclerview:recyclerview-selection:${Versions.recycleSelection}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val room = "androidx.room:room-ktx:${Versions.room}"
    const val maps = "com.google.maps.android:maps-ktx:${Versions.map}"
    const val mapUtils = "com.google.maps.android:maps-utils-ktx:${Versions.map}"
    const val serviceLocation = "com.google.android.gms:play-services-location:${Versions.serviceLocation}"
    const val lifecycleExt = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.archLifecycle}"

    // Coroutines
    const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutine}"
    const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutine}"
    const val coroutineNative = "org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.8"

    // Navigation
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    //Cards
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"

    const val multiDex = "com.android.support:multidex:${Versions.multiDex}"
    const val zXing = "com.journeyapps:zxing-android-embedded:${Versions.zxing}"

    // Viewpager2
    const val viewPager = "androidx.viewpager2:viewpager2:${Versions.viewPager}"

    //Play service

    // Moshi
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"

    // Retrofit with Moshi Converter
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"

    // Retrofit Coroutines Support
    const val retrofitCoroutineAdapter = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.retrofitCoroutinesAdapter}"

    // Glide
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"

    // RecyclerView
    const val recycleView = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"

    // Paging library
    const val paging = "androidx.paging:paging-runtime-ktx:${Versions.paging}"

    //Interceptor and http loggin
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"

    // Testing
    // Espresso
    // Core library
    // AndroidJUnitRunner and JUnit Rules
    const val runner = "androidx.test:runner:${Versions.test}"
    const val rules = "androidx.test:rules:${Versions.test}"
    const val coreTesting = "androidx.arch.core:core-testing:${Versions.archTesting}"

    // AndroidX Test - Instrumented testing
    const val androidJunit = "androidx.test.ext:junit:${Versions.androidX_test_ext_kotlin_runner}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val hamcrest = "org.hamcrest:hamcrest-all:${Versions.hamcrest}"
    const val coroutineeTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutine}"

    // Dependencies for Android instrumented unit tests
    const val jUnit = "junit:junit:${Versions.junit}"

    // AndroidX Test - JVM testing
    const val testX = "androidx.test:core-ktx:${Versions.androidX_test_core}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val jUnitX = "androidx.test.ext:junit-ktx:${Versions.androidX_test_ext_kotlin_runner}"
    const val jUnitExt = "androidx.test.ext:junit:${Versions.androidX_test_ext_kotlin_runner}"


    // Testing code should not be included in the main code.
    // Once https://issuetracker.google.com/128612536 is fixed this can be fixed.
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.fragment}"
    const val androidTestCore = "androidx.test:core:${Versions.androidX_test_core}"

    // Dependencies for Android instrumented unit tests
    const val mockito = "org.mockito:mockito-core:${Versions.mockito}"
    const val mockitoDex = "com.linkedin.dexmaker:dexmaker-mockito:${Versions.dex_maker}"

    //Test Espresso idling resources
    const val idlingRes = "androidx.test.espresso:espresso-idling-resource:${Versions.espresso}"

    //Custom login effect github
    const val loadingButton = "br.com.simplepass:loading-button-android:${Versions.loadingButton}"

    //Analytics
    const val analyticsKtx = "com.google.firebase:firebase-analytics-ktx:${Versions.analytics}"
    const val analytics = "com.google.firebase:firebase-analytics:${Versions.analytics}"

    const val workRuntime = "androidx.work:work-runtime-ktx:${Versions.work}"

    //Firebase Auth
    const val firebaseAuth = "com.google.firebase:firebase-auth-ktx:${Versions.auth}"

    //Firebase crashlytics
    const val crashLytics = "com.google.firebase:firebase-crashlytics:${Versions.crashlythics}"


    // Base Hilt dependencies:
    const val daggerCore = "com.google.dagger:dagger:2.37"
    const val daggerCoreCompiler = "com.google.dagger:dagger-compiler:2.37"

    // Dagger Android
    const val daggerAndroid = "com.google.dagger:dagger-android:2.37"
    const val daggerAndroidSupport = "com.google.dagger:dagger-android-support:2.37"
    const val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:2.37"

    const val androidHilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltNavigation = "androidx.hilt:hilt-navigation:1.0.0-alpha03"
    const val androidHiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"

    const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.lifeCycleViewModel}"

    const val hiltWork = "androidx.hilt:hilt-work:${Versions.lifeCycleViewModel}"
    const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:${Versions.hiltTest}"

    // ViewModel support dependencies:
    const val lifeCycleVm = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.lifeCycleViewModel}"

    // MockWebServer
    const val mockWebserver = "com.squareup.okhttp3:mockwebserver:${Versions.mockWebServer}"
    const val httpIdlingRes = "com.jakewharton.espresso:okhttp3-idling-resource:${Versions.httpIdling}"

    const val mavenAntTask = "org.apache.maven:maven-ant-tasks:${Versions.mavenAnt}"

    const val stepper = "com.ernestoyaquello.stepperform:vertical-stepper-form:${Versions.stepper}"

    const val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmer}"

    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    const val koinNative = "io.insert-koin:koin-core:${Versions.koin}"

    const val ktorNative = "io.ktor:ktor-client-core:${Versions.ktorVersion}"
    const val ktorAndroid = "io.ktor:ktor-client-android:${Versions.ktorVersion}"
    const val ktorIos = "io.ktor:ktor-client-ios:${Versions.ktorVersion}"
    const val ktorClientLogging = "io.ktor:ktor-client-logging:${Versions.ktorVersion}"
    const val ktorServerTest = "io.ktor:ktor-server-test-host:${Versions.ktorVersion}"
    const val ktorTest = "org.jetbrains.kotlin:kotlin-test:${Versions.ktorVersion}"

    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serializationVersion}"
    const val ktorSerialization = "io.ktor:ktor-client-serialization:${Versions.ktorVersion}"

    const val sqlDelightRuntime = "com.squareup.sqldelight:runtime:${Versions.sqlDelightVersion}"
    const val sqlDelightAndroid = "com.squareup.sqldelight:android-driver:${Versions.sqlDelightVersion}"
    const val sqlDelightNative = "com.squareup.sqldelight:native-driver:${Versions.sqlDelightVersion}"
    const val sqlDelightCoroutines = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelightVersion}"

    const val logBack = "ch.qos.logback:logback-classic:${Versions.logbackVersion}"

    const val moko = "dev.icerock.moko:mvvm:${Versions.mokoMvvmVersion}"


}