package rodrigosa.technews.retrofit.webclient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rodrigosa.technews.model.News
import rodrigosa.technews.retrofit.AppRetrofit
import rodrigosa.technews.retrofit.service.NewsService

private const val UNSUCCESSFUL_REQUEST = "unsuccessful_request"

class NewsWebClient(private val service: NewsService = AppRetrofit().newsService) {

    private fun <T> executeRequest(
        call: Call<T>,
        whenSuccess: (newNews: T?) -> Unit,
        whenFails: (error: String?) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    whenSuccess(response.body())
                } else {
                    whenFails(UNSUCCESSFUL_REQUEST)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                whenFails(t.message)
            }
        })
    }

    fun searchAll(
        whenSuccess: (newNews: List<News>?) -> Unit,
        whenFails: (error: String?) -> Unit
    ) {
        executeRequest(service.searchAll(), whenSuccess, whenFails)
    }

    fun save(
        news: News,
        whenSuccess: (newNews: News?) -> Unit,
        whenFails: (error: String?) -> Unit
    ) {
        executeRequest(service.save(news), whenSuccess, whenFails)
    }

    fun edit(
        id: Long,
        news: News,
        whenSuccess: (newNews: News?) -> Unit,
        whenFails: (error: String?) -> Unit
    ) {
        executeRequest(service.edit(id, news), whenSuccess, whenFails)
    }

    fun remove(
        id: Long,
        whenSuccess: (newNews: Void?) -> Unit,
        whenFails: (error: String?) -> Unit
    ) {
        executeRequest(service.remove(id), whenSuccess, whenFails)
    }

}