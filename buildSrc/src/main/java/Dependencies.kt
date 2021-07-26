import java.util.*

//This implementation based on this article: https://medium.com/doubletapp/how-to-manage-gradle-dependencies-in-the-android-project-proper-way-dad51fd4fe7
object Versions {

    //IDE
    const val gradleVersionsPlugin = "0.36.0"
    const val kotlin = "1.5.20"
    const val gradlePlugin = "4.2.1"

    //App
    //Kotlin
    const val androidCoreKtx = "1.5.0"

    //AndroidX
    const val androidLifeCycleRuntimeKtx = "2.4.0-alpha02"
    const val appCompat = "1.3.0"
    const val paging = "3.0.0"
    const val navVersion = "2.3.5"
    const val lifeCycleRuntime = "2.4.0-alpha02"

    //Design/UI
    const val material = "1.3.0"
    const val lottie = "3.7.0"
//    const val glide = "4.12.0"
    const val coil = "1.2.2"
    const val swipeRefreshLayout = "1.2.0-alpha01"

    //Networking
    const val retrofit = "2.9.0"
    const val moshi = "1.12.0"
    const val retrofitCoroutines = "0.9.2"
    const val retrofitLogInterceptor = "4.2.1"
    const val scarlet = "0.1.12"

    //LeakCanary
    const val leakCanary = "2.7"

    //Test
    const val jUnit = "4.13.2"
    const val androidJUnit = "1.1.2"
    const val espresso = "3.3.0"
}

object Dependencies {
    //IDE
    const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersionsPlugin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val gradlePlugin = "com.android.tools.build:gradle:${Versions.gradlePlugin}"

    //App
    //Kotlin
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"

    //AndroidX
    const val androidCoreKtx = "androidx.core:core-ktx:${Versions.androidCoreKtx}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val pagingKtx = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
    const val navigationKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navVersion}"
    const val navigationUIKTX = "androidx.navigation:navigation-ui-ktx:${Versions.navVersion}"
    const val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navVersion}"
    const val lifeCycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifeCycleRuntime}"

    //Design/UI
    const val material = "com.google.android.material:material:${Versions.material}"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
//    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
//    const val glideKapt = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val coil = "io.coil-kt:coil:${Versions.coil}"
    const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"

    //Networking
    const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshiKapt = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    const val retrofitCoroutines = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.retrofitCoroutines}"
    const val retrofitLogInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.retrofitLogInterceptor}"
    const val scarlet = "com.tinder.scarlet:scarlet:${Versions.scarlet}"
    const val scarletMoshi = "com.tinder.scarlet:message-adapter-moshi:${Versions.scarlet}"
    const val scarletOkHttpWebsocket = "com.tinder.scarlet:websocket-okhttp:${Versions.scarlet}"
    const val scarletLifeCycle = "com.tinder.scarlet:lifecycle-android:${Versions.scarlet}"
    const val scarletCoroutines = "com.tinder.scarlet:stream-adapter-coroutines:${Versions.scarlet}"

    //LeakCanary
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"

    //Test
    const val jUnit = "junit:junit:${Versions.jUnit}"
    const val androidJUnit = "androidx.test.ext:junit:${Versions.androidJUnit}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"

    //Used by the Gradle Versions Plugin
    @JvmStatic
    fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase(Locale.ROOT).contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }
}