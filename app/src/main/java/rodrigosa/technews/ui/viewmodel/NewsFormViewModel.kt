package rodrigosa.technews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import rodrigosa.technews.model.News
import rodrigosa.technews.repository.NewsRepository
import rodrigosa.technews.repository.Resource

class NewsFormViewModel(private val repository: NewsRepository) : ViewModel() {

    fun save(news: News): LiveData<Resource<Void?>> {
        return if (news.id > 0) {
            repository.edit(news)
        } else {
            repository.save(news)
        }
    }

    fun searchForId(id: Long) = repository.searchForId(id)

}