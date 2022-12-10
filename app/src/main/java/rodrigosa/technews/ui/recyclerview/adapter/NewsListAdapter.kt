package rodrigosa.technews.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import rodrigosa.technews.R
import rodrigosa.technews.model.News

class NewsListAdapter(
    private val context: Context,
    private val news: MutableList<News> = mutableListOf(),
    var whenItemClicked: (news: News) -> Unit = {}
): RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewCreated = LayoutInflater.from(context)
            .inflate(R.layout.item_news, parent, false)
        return ViewHolder(viewCreated)
    }

    override fun getItemCount() = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = news[position]
        holder.bind(news)
    }

    fun update(news: List<News>) {
        notifyItemRangeRemoved(0, this.news.size)
        this.news.clear()
        this.news.addAll(news)
        notifyItemRangeInserted(0, this.news.size)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var news: News

        init {
            itemView.setOnClickListener {
                if (::news.isInitialized) {
                    whenItemClicked(news)
                }
            }
        }

        fun bind(news: News) {
            this.news = news
            itemView.item_noticia_titulo.text = news.title
            itemView.item_noticia_texto.text = news.text
        }

    }

}