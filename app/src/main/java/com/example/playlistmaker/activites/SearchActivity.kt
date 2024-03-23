package com.example.playlistmaker.activites

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.activites.for_search.DataOfTracks
import com.example.playlistmaker.activites.for_search.MusicListAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.activites.for_search.TrackDataClass
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {

    private var countValue: String = AMOUNT_DEF
    private lateinit var searchLine: EditText
    private lateinit var clearButton:ImageButton

    private lateinit var recyclerTracks: RecyclerView
    private val originalTracksList = DataOfTracks().tracks.toMutableList()
    private val filteredTracksList = mutableListOf<TrackDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar: MaterialToolbar = findViewById(R.id.main_back_button)
        searchLine = findViewById(R.id.input_search)
        clearButton = findViewById(R.id.clear_button)

        recyclerTracks = findViewById(R.id.recyclerView)

        // вернуться назад
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // очистка поля поиска
        clearButton.setOnClickListener {
            searchLine.setText("")
            val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyboard.hideSoftInputFromWindow(searchLine.windowToken, 0)
        }

        if (savedInstanceState != null) {
            countValue = savedInstanceState.getString(PRODUCT_AMOUNT, AMOUNT_DEF)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                countValue = s.toString()
                filterOfTracks(s.toString())
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

    // фильтрация поиска
    private  fun filterOfTracks(query: String){
        filteredTracksList.clear()
        if(query.isEmpty()){
            filteredTracksList.addAll(originalTracksList)
        } else{
            filteredTracksList.addAll(originalTracksList.filter { it.trackName.contains(query, ignoreCase = true) })
        }

        recyclerTracks.adapter = MusicListAdapter(filteredTracksList)
        clearButton.visibility = clearButtonVisibility(query)
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
    }
}