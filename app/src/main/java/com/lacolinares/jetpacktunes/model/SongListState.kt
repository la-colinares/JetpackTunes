package com.lacolinares.jetpacktunes.model

import com.lacolinares.domain.model.Song

data class SongListState(
    val loading: Boolean = true,
    val songs: List<SongList> = emptyList(),
    val error: String = ""
){
    data class SongList(
        val category: String = "",
        val songs: List<Song> = emptyList(),
    )
}


