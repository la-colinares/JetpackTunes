package com.lacolinares.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SongResponse(
    val properties: Map<String, String>? = null,
    val tracks: List<Track>,
    val next: String = "",
)

@Serializable
data class Track(
    val layout: String = "",
    val type: String = "",
    val key: String = "",
    val title: String = "",
    val subtitle: String = "",
    val share: Share? = null,
    val images: Images = Images(),
    val hub: Hub = Hub(),
    val artists: List<Artist> = emptyList(),
    val url: String = "",
    @SerialName("highlightsurls")
    val highlightsUrls: HighlightsUrls? = null,
    val properties: Map<String, String>? = null,
)

@Serializable
data class Share(
    val subject: String = "",
    val text: String = "",
    val href: String = "",
    val image: String = "",
    val twitter: String = "",
    val html: String = "",
    val avatar: String = "",
    val snapchat: String = "",
)

@Serializable
data class Images(
    val background: String = "",
    @SerialName("coverart")
    val coverArt: String = "",
    @SerialName("coverarthq")
    val coverArtHq: String = "",
    @SerialName("joecolor")
    val joeColor: String = "",
)

@Serializable
data class Hub(
    val type: String = "",
    val image: String = "",
    val actions: List<Action> = emptyList(),
    val options: List<Option> = emptyList(),
    val explicit: Boolean = false,
    @SerialName("displayname")
    val displayName: String = "",
)

@Serializable
data class Action(
    val name: String = "",
    val type: String = "",
    val id: String? = null,
    val uri: String? = null,
)

@Serializable
data class Option(
    val caption: String = "",
    val actions: List<Action> = emptyList(),
    @SerialName("beacondata")
    val beaconData: BeaconData? = null,
    val image: String = "",
    val type: String = "",
    @SerialName("listcaption")
    val listCaption: String = "",
    @SerialName("overflowimage")
    val overflowImage: String = "",
    @SerialName("colouroverflowimage")
    val colourOverflowImage: Boolean = false,
    @SerialName("providername")
    val providerName: String = "",
)

@Serializable
data class BeaconData(
    val type: String = "",
    @SerialName("providername")
    val providerName: String = "",
)

@Serializable
data class Artist(
    val alias: String = "",
    val id: String = "",
    @SerialName("adamid")
    val adamId: String = "",
)

@Serializable
data class HighlightsUrls(
    @SerialName("artisthighlightsurl")
    val artistHighlightsUrl: String = "",
    @SerialName("trackhighlighturl")
    val trackHighlightUrl: String? = null,
)
