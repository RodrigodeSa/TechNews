package rodrigosa.technews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import rodrigosa.technews.model.News
import rodrigosa.technews.repository.NewsRepository
import rodrigosa.technews.repository.Resource

class NewsListViewModel(private val repository: NewsRepository) : ViewModel() {

    fun searchAll() : LiveData<Resource<List<News>?>> {
        return repository.searchAll()
    }

}