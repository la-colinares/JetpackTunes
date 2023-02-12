package com.lacolinares.data.mapper

import com.lacolinares.data.model.SongResponse
import com.lacolinares.domain.model.Song

fun SongResponse.toSongs(): List<Song> {
    return this.tracks.map {
        Song(
            key = it.key,
            title = it.title,
            subtitle = it.subtitle,
            backgroundImage = it.images.background,
            coverArt = it.images.coverArt,
            followUrl = it.url
        )
    }
}