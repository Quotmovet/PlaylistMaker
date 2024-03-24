package com.example.playlistmaker.activites

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api_itunes.ITunesAPI
import api_itunes.SearchTrackResponse
import com.example.playlistmaker.activites.for_search.MusicListAdapter
import com.example.playlistmaker.R
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var countValue: String = AMOUNT_DEF
    private lateinit var searchLine: EditText
    private lateinit var clearButton:ImageButton
    private lateinit var updateButton: Button

    private lateinit var searchErrorNothingFound: LinearLayout
    private lateinit var searchErrorNetwork: LinearLayout

    private lateinit var recyclerTracks: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val iTunesBaseUrl = getString(R.string.iTunesBaseUrl)

        val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val iTunesService = retrofit.create(ITunesAPI::class.java)

        val toolbar: MaterialToolbar = findViewById(R.id.main_back_button)
        searchLine = findViewById(R.id.input_search)
        clearButton = findViewById(R.id.clear_button)
        updateButton = findViewById(R.id.update_button)

        searchErrorNothingFound = findViewById(R.id.search_error_nothing_found)
        searchErrorNetwork = findViewById(R.id.search_error_network)

        recyclerTracks = findViewById(R.id.recyclerView)

        // Поиск трека с помощью iTunes
        fun searchTrackInItunes(){
            iTunesService.searchTrack(searchLine.text.toString()).enqueue(object : Callback<SearchTrackResponse>{
                override fun onResponse(
                    call: Call<SearchTrackResponse>,
                    response: Response<SearchTrackResponse> ) {
                    if (response.isSuccessful) {
                        if(response.body()?.resultCount == NULL_POINT) {
                            searchErrorNothingFound.visibility = View.VISIBLE
                            recyclerTracks.visibility = View.GONE
                        } else {
                            recyclerTracks.visibility = View.VISIBLE
                            searchErrorNothingFound.visibility = View.GONE
                            searchErrorNetwork.visibility = View.GONE
                            recyclerTracks.adapter = MusicListAdapter(response.body())
                        }
                    } else {
                        searchErrorNothingFound.visibility = View.VISIBLE
                        recyclerTracks.visibility = View.GONE
                        val errorBody = response.errorBody()?.string()
                        Log.e("RetrofitError", "Ошибка сервера: $errorBody")
                    }
                }

                override fun onFailure(call: Call<SearchTrackResponse>, t: Throwable) {
                    searchErrorNetwork.visibility = View.VISIBLE
                    recyclerTracks.visibility = View.GONE
                }
            })
        }

        // вернуться назад
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // очистка поля поиска
        clearButton.setOnClickListener {
            searchLine.setText("")
            val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyboard.hideSoftInputFromWindow(searchLine.windowToken, 0)
            searchErrorNetwork.visibility = View.GONE
            searchErrorNothingFound.visibility = View.GONE
        }

        if (savedInstanceState != null) {
            countValue = savedInstanceState.getString(PRODUCT_AMOUNT, AMOUNT_DEF)
        }

        // поиск !!!
        recyclerTracks.layoutManager = LinearLayoutManager(this)
        searchLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(searchLine.text.isNotEmpty()){
                    searchTrackInItunes()
                }
            }
            false
        }

        // обновить запрос
        updateButton.setOnClickListener {
            searchTrackInItunes()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                hideListIfSearchEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        searchLine.addTextChangedListener(simpleTextWatcher)
    }

    // сохранение значения поля поиска
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_AMOUNT, countValue)
    }

    // обновление значения поля поиска
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        countValue = savedInstanceState.getString(PRODUCT_AMOUNT, AMOUNT_DEF)
        searchLine.setText(countValue)
    }

    // видимость кнопки очистить поле ввода
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    // убрать список
    private fun hideListIfSearchEmpty() {
        if (searchLine.text.toString().isEmpty()) {
            recyclerTracks.visibility = View.GONE
        } else {
            recyclerTracks.visibility = View.VISIBLE
        }
    }

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
        const val AMOUNT_DEF = ""
        const val NULL_POINT = 0
    }
}