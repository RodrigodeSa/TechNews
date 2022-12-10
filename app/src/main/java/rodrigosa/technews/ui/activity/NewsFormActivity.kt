package rodrigosa.technews.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import rodrigosa.technews.R
import rodrigosa.technews.database.AppDatabase
import rodrigosa.technews.databinding.ActivityNewsFormBinding
import rodrigosa.technews.model.News
import rodrigosa.technews.repository.NewsRepository
import rodrigosa.technews.ui.activity.extensions.showError
import rodrigosa.technews.ui.viewmodel.NewsFormViewModel
import rodrigosa.technews.ui.viewmodel.factory.NewsFormViewModelFactory

private const val TITLE_EDIT_APPBAR = "Editing news"
private const val TITLE_CREATE_APPBAR = "Create news"
private const val MESSAGE_ERROR_SAVE = "could not save the news"

class NewsFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsFormBinding

    private val newsId: Long by lazy {
        intent.getLongExtra(NEWS_KEY_ID, 0)
    }
    private val viewModel by lazy {
        val repository = NewsRepository(AppDatabase.getInstance(this).newsDAO)
        val factory = NewsFormViewModelFactory(repository)
        ViewModelProviders.of(this, factory).get(NewsFormViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_form)
        definingTitle()
        fillOutForm()
    }

    private fun definingTitle() {
        title = if (newsId > 0) {
            TITLE_EDIT_APPBAR
        } else {
            TITLE_CREATE_APPBAR
        }
    }

    private fun fillOutForm() {
        viewModel.searchForId(newsId).observe(this, Observer { newsFound ->
            if (newsFound != null) {
                binding.activityNewsFormTitle.setText(newsFound.title)
                binding.activityNewsFormText.setText(newsFound.text)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.news_form_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.saved_news_form -> {
                val title = binding.activityNewsFormTitle.text.toString()
                val text = binding.activityNewsFormText.text.toString()
                save(News(newsId, title, text))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save(news: News) {
        viewModel.save(news).observe(this, Observer {
            if (it.error == null) {
                finish()
            } else {
                showError(MESSAGE_ERROR_SAVE)
            }
        })
    }

}