package com.example.playlistmaker.di

import org.koin.dsl.module
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import com.example.playlistmaker.search.data.network.ITunesAPI
import com.example.playlistmaker.search.data.storage.SearchHistoryImpl
import com.example.playlistmaker.search.domain.repository.SearchHistoryRepository
import org.koin.android.ext.koin.androidContext

val dataModule = module {

    single<ITunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("playlist_maker_preferences", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<SearchHistoryRepository> {
        SearchHistoryImpl(androidContext())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }
}