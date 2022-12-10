package rodrigosa.technews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rodrigosa.technews.repository.NewsRepository
import rodrigosa.technews.repository.Resource

class ViewNewsViewModel(id: Long, private val repository: NewsRepository) : ViewModel() {

    val newsFound = repository.searchForId(id)

    fun remove(): LiveData<Resource<Void?>> {
        return newsFound.value?.run {
            repository.remove(this)
        } ?: MutableLiveData<Resource<Void?>>().also {
            it.value = Resource(null, "News not found")
        }
    }

}