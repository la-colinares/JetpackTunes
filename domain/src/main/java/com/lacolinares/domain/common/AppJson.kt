package com.lacolinares.domain.common

import kotlinx.serialization.json.Json

object AppJson {

    val Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

}