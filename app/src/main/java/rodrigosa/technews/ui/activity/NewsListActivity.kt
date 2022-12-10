package rodrigosa.technews.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import rodrigosa.technews.R
import rodrigosa.technews.database.AppDatabase
import rodrigosa.technews.databinding.ActivityNewsListBinding
import rodrigosa.technews.model.News
import rodrigosa.technews.repository.NewsRepository
import rodrigosa.technews.ui.activity.extensions.showError
import rodrigosa.technews.ui.recyclerview.adapter.NewsListAdapter
import rodrigosa.technews.ui.viewmodel.NewsListViewModel
import rodrigosa.technews.ui.viewmodel.factory.NewsListViewModelFactory

private const val TITLE_APPBAR = "Notícias"
private const val MENSSAGE_FAIL_LOAD_NEWS = "failed to load the news"

class NewsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsListBinding

    private val adapter by lazy {
        NewsListAdapter(context = this)
    }
    private val viewModel by lazy {
        val repository = NewsRepository(AppDatabase.getInstance(this).newsDAO)
        val factory = NewsListViewModelFactory(repository)
        val provedor = ViewModelProviders.of(this, factory)
        provedor.get(NewsListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)
        title = TITLE_APPBAR
        configRecyclerView()
        configFabAddNews()
        searchNews()
    }

    private fun configFabAddNews() {
        binding.activityNewsListFabSaveNews.setOnClickListener {
            openFormCreationMode()
        }
    }

    private fun configRecyclerView() {
        val divisor = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.activityNewsListRecyclerview.addItemDecoration(divisor)
        binding.activityNewsListRecyclerview.adapter = adapter
        configAdapter()
    }

    private fun configAdapter() {
        adapter.whenItemClicked = this::openNewsViewer
    }

    private fun searchNews() {
        viewModel.searchAll().observe(this, Observer { resource ->
            resource.data?.let { adapter.update(it) }
            resource.error?.let {
                showError(MENSSAGE_FAIL_LOAD_NEWS)
            }
        })
    }

    private fun openFormCreationMode() {
        val intent = Intent(this, NewsFormActivity::class.java)
        startActivity(intent)
    }

    private fun openNewsViewer(it: News) {
        val intent = Intent(this, ViewNewsActivity::class.java)
        intent.putExtra(NEWS_KEY_ID, it.id)
        startActivity(intent)
    }

}