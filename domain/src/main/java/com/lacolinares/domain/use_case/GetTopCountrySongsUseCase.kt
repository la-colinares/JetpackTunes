package com.lacolinares.domain.use_case

import com.lacolinares.domain.model.Song
import com.lacolinares.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTopCountrySongsUseCase @Inject constructor(
    private val repository: SongRepository,
) {
//    operator fun invoke(country: String): Flow<Resource<List<Song>>> = flow {
//        try {
//            emit(Resource.Loading())
//            val musics = repository.getTopCountrySongs(country)
//            emit(Resource.Success(musics))
//        } catch (e: IOException) {
//            emit(Resource.Error(Constants.DEFAULT_ERR_MSG))
//        }
//    }

    suspend operator fun invoke(country: String): Flow<List<Song>> = flow {
        emit(repository.getTopCountryCharts(country))
    }
}