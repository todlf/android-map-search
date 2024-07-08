package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SearchDbHelper(context: Context) : SQLiteOpenHelper(context, "searchDb", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE ${SearchData.TABLE_NAME} (" +
                    "   ${SearchData.TABLE_COLUMN_NAME} varchar(255)," +
                    "   ${SearchData.TABLE_COLUMN_ADDRESS} varchar(255)," +
                    "   ${SearchData.TABLE_COLUMN_CATEGORY} varchar(255)" +
                    ");"
        )

        db?.execSQL(
            "CREATE TABLE ${SearchData.SAVED_SEARCH_TABLE_NAME} (" +
                    "   ${SearchData.SAVED_SEARCH_COLUMN_NAME} varchar(255)" +
                    ");"
        )

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${SearchData.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${SearchData.SAVED_SEARCH_TABLE_NAME}")
        onCreate(db)
    }

    fun getAllSavedWords(): List<String> {
        val db = readableDatabase
        val cursor = db.query(
            SearchData.SAVED_SEARCH_TABLE_NAME,
            arrayOf(SearchData.SAVED_SEARCH_COLUMN_NAME),
            null,
            null,
            null,
            null,
            null
        )
        val savedWords = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                savedWords.add(getString(getColumnIndexOrThrow(SearchData.SAVED_SEARCH_COLUMN_NAME)))
            }
        }
        cursor.close()
        return savedWords
    }
}