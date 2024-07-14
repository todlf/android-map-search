package campus.tech.kakao.map

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var db: SearchDbHelper

    private lateinit var searchWord: EditText
    private lateinit var deleteSearchWord: Button
    private lateinit var searchNothing: TextView

    private lateinit var savedSearchWordRecyclerView: RecyclerView
    private lateinit var savedSearchAdapter: SavedSearchAdapter

    private var searchDataList = mutableListOf<SearchData>()
    private var savedSearchList = mutableListOf<String>()

    private val Authorization = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = SearchDbHelper(context = this)



        recyclerView = findViewById(R.id.recyclerView)
        searchWord = findViewById(R.id.searchWord)
        deleteSearchWord = findViewById(R.id.deleteSearchWord)
        searchNothing = findViewById(R.id.searchNothing)
        savedSearchWordRecyclerView = findViewById(R.id.savedSearchWordRecyclerView)

        adapter = SearchAdapter()


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        savedSearchAdapter = SavedSearchAdapter()

        savedSearchWordRecyclerView.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )

        savedSearchWordRecyclerView.adapter = savedSearchAdapter

        itemClickSaveWord()
        deleteItem()
        showDb()
        deleteWord()
        saveData()
        loadSavedWords()


        searchWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchTerm = s.toString()
                if (searchTerm.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    savedSearchWordRecyclerView.visibility = View.GONE
                } else {
                    filterByCategory(searchTerm)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun saveData() {
        lifecycleScope.launch {
            try {
                db.saveDb(Authorization)
            } catch (e: Exception) {
                // 예외 처리
                e.printStackTrace()
            }
        }
    }

    private fun showDb() {
        lifecycleScope.launch {
            searchDataList = db.loadDb().toMutableList()

            if (searchWord.text.isEmpty()) {
                adapter.searchDataList = emptyList()
                recyclerView.visibility = View.GONE
                searchNothing.visibility = View.VISIBLE
                savedSearchWordRecyclerView.visibility = View.GONE
            } else {
                adapter.searchDataList = searchDataList
                recyclerView.visibility = View.VISIBLE
                searchNothing.visibility = View.GONE
                savedSearchWordRecyclerView.visibility = View.VISIBLE
            }
            Log.e("Retrofit", "SearchDataList 찾기1: ${searchDataList}")
            adapter.notifyDataSetChanged()
        }
    }

    private fun deleteWord() {
        deleteSearchWord.setOnClickListener {
            searchWord.text.clear()
            showDb()
        }
    }

    private fun filterByCategory(category: String) {
        val filteredList = searchDataList.filter { it.category == category }
        adapter.searchDataList = filteredList
        adapter.notifyDataSetChanged()

        if (filteredList.isEmpty()) {
            recyclerView.visibility = View.GONE
            searchNothing.visibility = View.VISIBLE
            savedSearchWordRecyclerView.visibility = View.GONE
        } else {
            recyclerView.visibility = View.VISIBLE
            searchNothing.visibility = View.GONE
            savedSearchWordRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun loadSavedWords() {
        savedSearchList = db.getAllSavedWords().toMutableList()
        savedSearchAdapter.savedSearchList = savedSearchList
        savedSearchAdapter.notifyDataSetChanged()
    }

    private fun itemClickSaveWord() {
        adapter.setItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val searchData = adapter.searchDataList[position]
                val wDb = db.writableDatabase
                val values = ContentValues()

                values.put(SearchData.SAVED_SEARCH_COLUMN_NAME, searchData.name)
                wDb.insert(SearchData.SAVED_SEARCH_TABLE_NAME, null, values)
                values.clear()
                savedSearchList = db.getAllSavedWords().toMutableList()
                savedSearchAdapter.savedSearchList = savedSearchList
                savedSearchAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun deleteItem() {
        savedSearchAdapter.setOnDeleteClickListener(object :
            SavedSearchAdapter.OnDeleteClickListener {
            override fun onDeleteClick(position: Int) {
                val deletedWord = savedSearchAdapter.savedSearchList[position]
                val wDb = db.writableDatabase
                wDb.delete(
                    SearchData.SAVED_SEARCH_TABLE_NAME,
                    "${SearchData.SAVED_SEARCH_COLUMN_NAME} = ?",
                    arrayOf(deletedWord)
                )

                savedSearchAdapter.savedSearchList.removeAt(position)
                savedSearchAdapter.notifyItemRemoved(position)

            }
        })
    }

}



