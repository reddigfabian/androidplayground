package com.fabian.androidplayground.api.picsum

import android.os.Parcelable
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


object  LoremPicsumApi {
    val loremPicsumService : LoremPicsumService by lazy {
        retrofit.create(LoremPicsumService::class.java)
    }
}

private const val BASE_URL = "https://picsum.photos/v2/"
private const val LIST_ENDPOINT = "list"

var interceptor  = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BASIC
}
var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
//    .client(client)
    .build()

interface LoremPicsumService{
    @GET(LIST_ENDPOINT)
    fun imageListAsync(@Query("limit") limit: Int?, @Query("page") page: Int?): Deferred<List<Picsum>>
}


@Parcelize
data class Picsum(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val download_url: String
): Parcelable {
    val heightRatio get() = height.toDouble()/width.toDouble()
}