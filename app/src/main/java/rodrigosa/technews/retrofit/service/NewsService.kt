package rodrigosa.technews.retrofit.service

import retrofit2.Call
import retrofit2.http.*
import rodrigosa.technews.model.News

interface NewsService {

    @GET("news")
    fun searchAll(): Call<List<News>>

    @POST("news")
    fun save(@Body news: News): Call<News>

    @PUT("news/{id}")
    fun edit(@Path("id") id: Long, @Body news: News) : Call<News>

    @DELETE("news/{id}")
    fun remove(@Path("id") id: Long): Call<Void>

}