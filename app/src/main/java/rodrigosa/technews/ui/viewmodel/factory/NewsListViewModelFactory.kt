package rodrigosa.technews.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import rodrigosa.technews.repository.NewsRepository
import rodrigosa.technews.ui.viewmodel.NewsListViewModel

class NewsListViewModelFactory(private val repository: NewsRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsListViewModel(repository) as T
    }
}