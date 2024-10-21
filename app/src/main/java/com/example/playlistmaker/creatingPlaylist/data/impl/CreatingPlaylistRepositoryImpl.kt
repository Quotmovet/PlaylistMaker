package com.example.playlistmaker.creatingPlaylist.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.example.playlistmaker.creatingPlaylist.data.db.entity.PlaylistEntity
import com.example.playlistmaker.creatingPlaylist.domain.repository.CreatingPlaylistRepository
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.player.data.db.entity.TrackFromPlaylistsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CreatingPlaylistRepositoryImpl (
    private val context: Context,
    private val appDatabase: AppDatabase,
): CreatingPlaylistRepository {

    override fun addNewPlaylist(playlist: PlaylistEntity): Flow<Long> = flow {
        val id = appDatabase.getPlaylistDao().insertPlaylist(playlist)
        emit(id)
    }

    override fun updatePlaylist(playlist: PlaylistEntity): Flow<Int> = flow {
        val count = appDatabase.getPlaylistDao().updatePlaylist(playlist)
        emit(count)
    }

    override fun saveImageToPrivateStorage(uri: String): String {

        // создание каталога для хранения обложки
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        // создание имени файла
        val zdt = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yy-MM-dd_HH-mm-ss")
        val fileName = formatter.format(zdt) + ".jpg"
        val file = File(filePath, fileName)

        // получаем исходящий поток байтов
        val inputStream = context.contentResolver.openInputStream(uri.toUri())

        // создаем поток для записи
        val outputStream = FileOutputStream(file)

        // конвертируем байты в картинку
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri().toString()
    }

    // Добавление трека в таблицу 'tracks_from_playlists_table'
    override fun addTrackInPlaylist(track: TrackFromPlaylistsEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                appDatabase.getTracksFromPlaylistsDao().insertTrack(track)
                Log.d("CreatingPlaylistRepo", "Track inserted: ${track.trackId}")
            } catch (e: Exception) {
                Log.e("CreatingPlaylistRepo", "Error inserting track: ${e.message}")
            }
        }
    }
}