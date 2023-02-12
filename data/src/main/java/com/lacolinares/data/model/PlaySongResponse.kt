package com.lacolinares.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PlaySongResponse(
    @SerialName("download")
    val songUrl: String
)
