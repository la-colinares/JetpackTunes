package com.lacolinares.domain.use_case

import com.lacolinares.domain.model.Song
import com.lacolinares.domain.repository.SongRepository
import javax.inject.Inject

class GetTopTracksUseCase @Inject constructor(
    private val repository: SongRepository,
) {
    suspend operator fun invoke(country: String, genre: Int): List<Song> {
        return repository.getTopTracks(country, genre)
    }
}