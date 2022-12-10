package rodrigosa.technews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import rodrigosa.technews.asynctask.BaseAsyncTask
import rodrigosa.technews.database.dao.NewsDAO
import rodrigosa.technews.model.News
import rodrigosa.technews.retrofit.webclient.NewsWebClient

class NewsRepository(
    private val dao: NewsDAO,
    private val webclient: NewsWebClient = NewsWebClient()
) {

    private val mediator = MediatorLiveData<Resource<List<News>?>>()

    fun searchAll(): LiveData<Resource<List<News>?>> {

        mediator.addSource(searchInternal()) { newsFound ->
            mediator.value = Resource(newsFound)
        }

        val failureWebApiLiveData = MutableLiveData<Resource<List<News>?>>()
        mediator.addSource(failureWebApiLiveData) { failResource ->
            val currentResource = mediator.value
            val newFeature: Resource<List<News>?> = if (currentResource != null) {
                Resource(currentResource.data, failResource.error)
            } else {
                failResource
            }
            mediator.value = newFeature
        }

        searchOnApi(whenFails = { error ->
            failureWebApiLiveData.value = Resource(null, error)
        })

        return mediator
    }

    fun save(news: News): LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
        saveOnApi(news,
            whenSuccess = {
                liveData.value = Resource(null)
            }, whenFails = { error ->
                liveData.value = Resource(null, error)
            })
        return liveData
    }

    fun remove(news: News): LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
        removeOnApi(news,
            whenSuccess = {
                liveData.value = Resource(null)
            }, whenFails = { erro ->
                liveData.value = Resource(null, erro)
            })
        return liveData
    }

    fun edit(news: News): LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
        editOnApi(news,
            whenSuccess = {
                liveData.value = Resource(null)
            }, whenFails = { error ->
                liveData.value = Resource(null, error)
            })
        return liveData
    }

    fun searchForId(newsId: Long): LiveData<News?> {
        return dao.searchForId(newsId)
    }

    private fun searchOnApi(whenFails: (error: String?) -> Unit) {
        webclient.searchAll(
            whenSuccess = { newNews ->
                newNews?.let {
                    internalSave(newNews)
                }
            }, whenFails = whenFails
        )
    }

    private fun searchInternal(): LiveData<List<News>> {
        return dao.searchAll()
    }

    private fun saveOnApi(
        news: News,
        whenSuccess: () -> Unit,
        whenFails: (error: String?) -> Unit
    ) {
        webclient.save(
            news,
            whenSuccess = {
                it?.let { savedNews ->
                    internalSave(savedNews, whenSuccess)
                }
            }, whenFails = whenFails
        )
    }

    private fun internalSave(news: List<News>) {
        BaseAsyncTask(
            whenExecute = {
                dao.save(news)
            }, whenEnd = {}
        ).execute()
    }

    private fun internalSave(news: News, whenSuccess: () -> Unit) {
        BaseAsyncTask(
            whenExecute = {
                dao.save(news)
            }, whenEnd = {
                whenSuccess()
            }).execute()
    }

    private fun removeOnApi(
        news: News,
        whenSuccess: () -> Unit,
        whenFails: (error: String?) -> Unit
    ) {
        webclient.remove(
            news.id,
            whenSuccess = {
                removeInternal(news, whenSuccess)
            },
            whenFails = whenFails
        )
    }

    private fun removeInternal(news: News, whenSuccess: () -> Unit) {
        BaseAsyncTask(
            whenExecute = {
                dao.remove(news)
            }, whenEnd = {
                whenSuccess()
            }).execute()
    }

    private fun editOnApi(
        news: News,
        whenSuccess: () -> Unit,
        whenFails: (error: String?) -> Unit
    ) {
        webclient.edit(
            news.id, news,
            whenSuccess = { editedNews ->
                editedNews?.let {
                    internalSave(editedNews, whenSuccess)
                }
            }, whenFails = whenFails
        )
    }

}