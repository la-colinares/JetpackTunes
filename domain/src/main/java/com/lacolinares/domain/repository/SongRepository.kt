package com.lacolinares.domain.repository

import com.lacolinares.domain.model.Song

interface SongRepository {
    suspend fun getTopWorldCharts(): List<Song>
    suspend fun getTopCountryCharts(country: String): List<Song>
    suspend fun getTopTracks(country: String, genre: Int): List<Song>
    suspend fun playSong(title: String, artist: String): String
    suspend fun getRecommendedTracks(id: String): List<Song>
}