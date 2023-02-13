package com.lacolinares.domain.use_case

import com.lacolinares.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSongLinkUseCase @Inject constructor(
    private val repository: SongRepository
) {
    suspend operator fun invoke(title: String, artist: String): Flow<String> = flow {
        emit(repository.playSong(title, artist))
    }
}