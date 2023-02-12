package com.lacolinares.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lacolinares.data.api.MusicServiceApi
import com.lacolinares.data.repository.SongRepositoryImpl
import com.lacolinares.domain.common.AppJson
import com.lacolinares.domain.common.Constants.BASE_URL
import com.lacolinares.domain.repository.SongRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Module
@InstallIn(SingletonComponent::class)
object NetworkDataModule {

    @Provides
    @Singleton
    fun provideMusicServiceApi(): MusicServiceApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(AppJson.Json.asConverterFactory(contentType))
            .build()
            .create(MusicServiceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSongRepository(api: MusicServiceApi): SongRepository{
        return SongRepositoryImpl(api)
    }

}