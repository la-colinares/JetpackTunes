package com.lacolinares.data.api

import com.lacolinares.data.model.PlaySongResponse
import com.lacolinares.data.model.SongResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicServiceApi {

    @GET("chart/world")
    suspend fun getTopWorldCharts(): SongResponse

    @GET("chart/country")
    suspend fun getTopCountryCharts(@Query("country") country: String): SongResponse

    @GET("chart/world/genre")
    suspend fun getTopTracks(
        @Query("country") country: String,
        @Query("genre") genre: Int,
    ): SongResponse

    @GET("play")
    suspend fun playSong(
        @Query("artist", encoded = true) artist: String,
        @Query("title", encoded = true) title: String,
    ): PlaySongResponse

    @GET("shazam-songs/similarities")
    suspend fun getRecommendedTracks(@Query("id") id: String): SongResponse

}