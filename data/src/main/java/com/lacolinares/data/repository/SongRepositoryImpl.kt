package com.lacolinares.data.repository

import com.lacolinares.data.api.MusicServiceApi
import com.lacolinares.data.mapper.toSongs
import com.lacolinares.domain.model.Song
import com.lacolinares.domain.repository.SongRepository
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val api: MusicServiceApi
) : SongRepository {

    override suspend fun getTopWorldCharts(): List<Song> {
        return api.getTopWorldCharts().toSongs()
    }

    override suspend fun getTopCountryCharts(country: String): List<Song> {
        return api.getTopCountryCharts(country).toSongs()
    }

    override suspend fun getTopTracks(country: String, genre: Int): List<Song> {
        return api.getTopTracks(country, genre).toSongs()
    }

    override suspend fun playSong(title: String, artist: String): String {
        return api.playSong(artist, title).songUrl
    }

    override suspend fun getRecommendedTracks(id: String): List<Song> {
        return if (id.isNotEmpty()) api.getRecommendedTracks(id).toSongs() else emptyList()
    }

}