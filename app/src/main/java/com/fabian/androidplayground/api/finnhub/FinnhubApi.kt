package com.fabian.androidplayground.api.finnhub

import android.os.Parcelable
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


object  FinnhubApi {
    val finnhubApiService : FinnhubApiService by lazy {
        scarlet.create(FinnhubApiService::class.java)
    }
}

private const val BASE_URL = "https://picsum.photos/v2/"

var interceptor  = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BASIC
}
var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val scarlet = Scarlet.Builder()
    .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
    .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
    .build()

interface FinnhubApiService{
    @Send
    fun sendSubscribe(subscribe: Subscribe)
    @Receive
    fun observeTicker() : ReceiveChannel<Ticker>
    @Receive
    fun observeOnConnectionOpenedEvent(): ReceiveChannel<WebSocket.Event.OnConnectionOpened<*>>
}

@Parcelize
data class Subscribe(
    val type: String,
    val symbol: String
): Parcelable

/*
{"data":[{"c":["1","24","12"],"p":3557.25,"s":"AMZN","t":1626732221656,"v":2}],"type":"trade"}
 */
@Parcelize
data class Ticker(
    val data: List<TickerData>,
    val type: String
): Parcelable

@Parcelize
data class TickerData(
    val c: List<String>,
    val p: Double,
    val s: String,
    val t: Long,
    val v: Int
): Parcelable