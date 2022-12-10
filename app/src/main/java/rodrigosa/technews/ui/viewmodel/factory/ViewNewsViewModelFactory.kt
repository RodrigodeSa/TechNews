package rodrigosa.technews.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import rodrigosa.technews.repository.NewsRepository
import rodrigosa.technews.ui.viewmodel.ViewNewsViewModel

class ViewNewsViewModelFactory(private val id: Long, private val repository: NewsRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewNewsViewModel(id, repository) as T
    }

}