package rodrigosa.technews.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import rodrigosa.technews.R
import rodrigosa.technews.database.AppDatabase
import rodrigosa.technews.databinding.ActivityViewNewsBinding
import rodrigosa.technews.model.News
import rodrigosa.technews.repository.NewsRepository
import rodrigosa.technews.ui.activity.extensions.showError
import rodrigosa.technews.ui.viewmodel.ViewNewsViewModel
import rodrigosa.technews.ui.viewmodel.factory.ViewNewsViewModelFactory

private const val NEWS_NOT_FOUND = "News not found"
private const val TITLE_APPBAR = "News"
private const val MENSAGEM_REMOVE_FAILED = "Unable to remove news"

class ViewNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewNewsBinding

    private val newsId: Long by lazy {
        intent.getLongExtra(NEWS_KEY_ID, 0)
    }
    private val viewModel by lazy {
        val repository = NewsRepository(AppDatabase.getInstance(this).newsDAO)
        val factory = ViewNewsViewModelFactory(newsId, repository)
        ViewModelProviders.of(this, factory).get(ViewNewsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_news)
        title = TITLE_APPBAR
        checkNewsId()
        searchSelectedNews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_news_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.view_news_menu_edit -> openEditForm()
            R.id.view_news_menu_remove -> remove()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchSelectedNews() {
        viewModel.newsFound.observe(this, Observer { newsFound ->
            newsFound?.let {
                fillFields(it)
            }
        })
    }

    private fun checkNewsId() {
        if (newsId == 0L) {
            showError(NEWS_NOT_FOUND)
            finish()
        }
    }

    private fun fillFields(news: News) {
        binding.activityViewNewsTitle.text = news.title
        binding.activityViewNewsText.text = news.text
    }

    private fun remove() {
        viewModel.remove().observe(this, Observer {
            if (it.error == null) {
                finish()
            } else {
                showError(MENSAGEM_REMOVE_FAILED)
            }
        })
    }

    private fun openEditForm() {
        val intent = Intent(this, NewsFormActivity::class.java)
        intent.putExtra(NEWS_KEY_ID, newsId)
        startActivity(intent)
    }

}