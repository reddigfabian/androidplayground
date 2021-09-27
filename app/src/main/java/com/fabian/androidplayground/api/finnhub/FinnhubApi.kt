package com.fabian.androidplayground.api.finnhub

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.fabian.androidplayground.BuildConfig
import com.fabian.androidplayground.common.scarlet.FlowStreamAdapterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.Stream
import com.tinder.scarlet.StreamAdapter
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.Type

object  FinnhubApi {
    val finnhubApiRetrofitService : FinnhubApiRetrofitService by lazy {
        retrofit.create(FinnhubApiRetrofitService::class.java)
    }
    val finnhubApiScarletService : FinnhubApiScarletService by lazy {
        scarlet.create(FinnhubApiScarletService::class.java)
    }
}

private const val BASE_URL = "https://finnhub.io/api/v1/"
private const val BASE_WEBSOCKET = "wss://ws.finnhub.io/?token=${BuildConfig.FINNHUB_API}"
private const val STOCK_SYMBOL_ENDPOINT = "stock/symbol"
private const val STOCK_QUOTE_ENDPOINT = "quote"
//
//var interceptor  = HttpLoggingInterceptor().apply {
//    level = HttpLoggingInterceptor.Level.BASIC
//}
var client: OkHttpClient = OkHttpClient.Builder()
//    .addInterceptor(interceptor)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(client)
    .build()

private val scarlet = Scarlet.Builder()
    .webSocketFactory(client.newWebSocketFactory(BASE_WEBSOCKET))
    .addMessageAdapterFactory(MoshiMessageAdapter.Factory(moshi))
    .addStreamAdapterFactory(FlowStreamAdapterFactory())
    .build()

interface FinnhubApiRetrofitService{
    @GET(STOCK_SYMBOL_ENDPOINT)
    fun symbolList(@Query("exchange") exchange : String, @Query("token") token : String = BuildConfig.FINNHUB_API_SANDBOX): Deferred<List<FinnhubStockSymbol>>
    @GET(STOCK_QUOTE_ENDPOINT)
    fun symbolQuote(@Query("symbol") symbol : String, @Query("token") token : String = BuildConfig.FINNHUB_API_SANDBOX): Deferred<SymbolQuote>


}

interface FinnhubApiScarletService{
    @Send
    fun sendSubscribe(subscribe: Subscribe)
    @Send
    fun sendUnsubscribe(unsubscribe: UnSubscribe)
    @Receive
    fun observeTicker() : Flow<Ticker>
    @Receive
    fun observeWebSocketEvent(): Flow<WebSocket.Event>
}

@Parcelize
data class Subscribe(
    val symbol: String,
    val type: String = "subscribe"
): Parcelable

@Parcelize
data class UnSubscribe(
    val symbol: String,
    val type: String = "unsubscribe"
): Parcelable

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

@Parcelize
data class FinnhubStockSymbol(
    val currency: String,
    val description: String,
    val displaySymbol: String,
    val figi: String,
    val symbol: String,
    val type: String
): Parcelable {
    var quote : SymbolQuote? = null
    val stockName = "$description ($symbol)"
}

@Parcelize
data class SymbolQuote(
    val c: Double,
    val h: Double,
    val l: Double,
    val o: Double,
    val pc: Double,
    val t: Long
): Parcelable