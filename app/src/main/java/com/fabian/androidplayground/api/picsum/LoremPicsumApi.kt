package com.fabian.androidplayground.api.picsum

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fabian.androidplayground.BR
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.recyclerview.viewmodels.RecyclerItem
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.parcel.Parcelize
import okhttp3.OkHttpClient
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

interface LoremPicsumService{
    @GET(LIST_ENDPOINT)
    suspend fun imageList(@Query("limit") limit: Int?, @Query("page") page: Int?): List<Picsum>
}

@Parcelize
@Entity(tableName = "picsum")
data class Picsum(
    @PrimaryKey val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val download_url: String
): Parcelable {
    val heightRatio get() = height.toDouble()/width.toDouble()

    fun toRecyclerItem() =
        RecyclerItem(
            data = this,
            variableId = BR.picsum,
            layoutID = R.layout.item_lorem_picsum_room_list
        )
}