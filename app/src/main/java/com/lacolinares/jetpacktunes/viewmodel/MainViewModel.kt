package com.lacolinares.jetpacktunes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lacolinares.domain.common.Constants.DEFAULT_COUNTRY
import com.lacolinares.domain.common.SongCategory
import com.lacolinares.domain.model.Song
import com.lacolinares.domain.use_case.GetTopCountrySongsUseCase
import com.lacolinares.domain.use_case.GetTopTracksUseCase
import com.lacolinares.domain.use_case.GetTopWorldTracksUseCase
import com.lacolinares.jetpacktunes.model.SongListState
import com.lacolinares.jetpacktunes.model.SuggestedSongState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTopWorldTracksUseCase: GetTopWorldTracksUseCase,
    private val getTopCountrySongsUseCase: GetTopCountrySongsUseCase,
    private val getTopTracksUseCase: GetTopTracksUseCase,
) : ViewModel() {

    private val categories = SongCategory.getTracksCategories()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _suggestedSongState = MutableStateFlow(SuggestedSongState())
    val suggestedSongState: StateFlow<SuggestedSongState> = _suggestedSongState.asStateFlow()

    private val _topWorldCharts = MutableStateFlow<List<Song>>(emptyList())
    private val _topCountryCharts = MutableStateFlow<List<Song>>(emptyList())
    private val _topTracks = MutableStateFlow<List<Pair<SongCategory, List<Song>>>>(emptyList())

    val songListState = combine(_topWorldCharts, _topCountryCharts, _topTracks) { worldCharts, countryCharts, topTracks ->
        if (worldCharts.isNotEmpty() && countryCharts.isNotEmpty() && topTracks.isNotEmpty()) {
            val topWorldCategory = SongListState.SongList(category = SongCategory.TOP_WORLD_CHART.value, songs = worldCharts)
            val topCountryCategory = SongListState.SongList(category = SongCategory.TOP_COUNTRY_CHART.value, songs = countryCharts)

            val topTracksList = topTracks.map {
                val category = it.first.value
                val songs = it.second
                SongListState.SongList(category = category, songs = songs)
            }

            val songs = listOf(topWorldCategory, topCountryCategory) + topTracksList
            SongListState(loading = false, songs = songs)
        } else {
            SongListState(loading = true, songs = emptyList())
        }
    }

    init {
        getTopWorldTracks()
        getTopCountryTracks()
        getTopTracks()
    }

    private fun getTopTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            val topTracks = categories.map {
                async(Dispatchers.IO) {
                    Pair(it, getTopTracksUseCase(country = DEFAULT_COUNTRY, genre = it.genre))
                }
            }.awaitAll()
            _topTracks.update { topTracks }
        }
    }

    private fun getTopCountryTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            getTopCountrySongsUseCase(DEFAULT_COUNTRY).collect { songs ->
                _topCountryCharts.update { songs }
            }
        }
    }

    private fun getTopWorldTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            getTopWorldTracksUseCase().collect { songs ->
                // pick one random song from top world tracks
                val suggestedSong = songs.shuffled().first()
                _suggestedSongState.update {
                    SuggestedSongState(loading = suggestedSong.title.isEmpty(), song = suggestedSong)
                }

                _topWorldCharts.update { songs }
            }
        }
    }

    fun onSearchSong(text: String) {
        _searchText.value = text
    }
}