package com.lacolinares.jetpacktunes.model

data class PlayAudioState(
    val loading: Boolean = true,
    val audioData: AudioData = AudioData(),
    val error: String = ""
){
    data class AudioData(
        val audioUrl: String = "",
        val image: String = "",
        val title: String = "",
        val artist: String = "",
        val audioStartTime: String = "",
        val audioEndTime: String = "",
    )
}