package com.example.playlistmaker.di

import org.koin.dsl.module
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.playlistmaker.creatingPlaylist.data.db.converter.PlaylistDbConverter
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.search.data.network.ITunesAPI
import com.example.playlistmaker.search.data.storage.SearchHistoryImpl
import com.example.playlistmaker.search.data.datasource.SearchHistoryLocalDataSource
import org.koin.android.ext.koin.androidContext

val dataModule = module {

    single<ITunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    single<SharedPreferences> {
        get<Context>().getSharedPreferences("playlist_maker_preferences", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<SearchHistoryLocalDataSource> {
        SearchHistoryImpl(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single{
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { PlaylistDbConverter(get()) }
}