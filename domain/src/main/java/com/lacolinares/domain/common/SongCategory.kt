package com.lacolinares.domain.common

enum class SongCategory(val genre: Int, val value: String) {
    RECOMMENDED(genre = -2, value = "Recommended for you"),
    TOP_WORLD_CHART(genre = -1, value = "Top world chart"),
    TOP_COUNTRY_CHART(genre = 0, value = "Top country chart"),
    TOP_TRACKS_POP(genre = 1, value = "Top tracks (Pop)"),
    TOP_TRACKS_HIPHOP(genre = 2, value = "Top tracks (Hiphop Rap)"),
    TOP_TRACKS_DANCE(genre = 3, value = "Top tracks (Dance)"),
    TOP_TRACKS_ELECTRONIC(genre = 4, value = "Top tracks (Electronic)"),
    TOP_TRACKS_RND(genre = 5, value = "Top tracks (RND soul)"),
    TOP_TRACKS_ALTERNATIVE(genre = 6, value = "Top tracks (Alternative)"),
    TOP_TRACKS_ROCK(genre = 7, value = "Top tracks (Rock)"),
    TOP_TRACKS_LATIN(genre = 8, value = "Top tracks (Latin)"),
    TOP_TRACKS_FILM_TV(genre = 9, value = "Top tracks (Film TV sage)"),
    TOP_TRACKS_COUNTRY(genre = 10, value = "Top tracks (Country)"),
    TOP_TRACKS_AFRO_BEATS(genre = 11, value = "Top tracks (Afro bets)"),
    TOP_TRACKS_WORLDWIDE(genre = 12, value = "Top tracks (World wide)"),
    TOP_TRACKS_REGGAE(genre = 13, value = "Top tracks (Reggae)"),
    TOP_TRACKS_HOUSE(genre = 14, value = "Top tracks (House)"),
    TOP_TRACKS_KOREAN_POP(genre = 15, value = "Top tracks (Kpop)"),
    TOP_TRACKS_FRENCH_POP(genre = 16, value = "Top tracks (French Pop)"),
    TOP_TRACKS_SINGER_SONGWRITER(genre = 17, value = "Top tracks (Singer Songwriter)");

    companion object {
        fun getTracksCategories(): List<SongCategory> {
            return values().filter { it.genre > 0 }
        }
    }
}