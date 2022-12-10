package rodrigosa.technews.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import rodrigosa.technews.database.dao.NewsDAO
import rodrigosa.technews.model.News

private const val NOME_BANCO_DE_DADOS = "news.db"

@Database(entities = [News::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val newsDAO: NewsDAO

    companion object {

        private lateinit var db: AppDatabase

        fun getInstance(context: Context): AppDatabase {

            if(::db.isInitialized) return db

            db = Room.databaseBuilder(context, AppDatabase::class.java, NOME_BANCO_DE_DADOS
            ).build()

            return db
        }

    }

}