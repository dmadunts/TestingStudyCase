package com.dmadunts.testingstudycase.data.remote.apis

import com.dmadunts.testingstudycase.data.ApiKeys
import com.dmadunts.testingstudycase.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("/api/")
    suspend fun searchImage(
        @Query("q") query: String,
        @Query("key") apiKey: String = ApiKeys.PIXABAY_API_KEY
    ): Response<ImageResponse>
}