package com.dmadunts.testingstudycase.data.remote.responses

data class ImageResult(
    val hits: List<ImageResult>,
    val total: Int,
    val totalHits: Int
)