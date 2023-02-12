package com.lacolinares.jetpacktunes.model

import com.lacolinares.domain.model.Song

data class SuggestedSongState(
    val loading: Boolean = true,
    val song: Song = Song(),
    val error: String = ""
)
