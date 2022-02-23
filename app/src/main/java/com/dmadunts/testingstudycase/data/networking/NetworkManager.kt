package com.dmadunts.testingstudycase.data.networking

import com.dmadunts.testingstudycase.utils.Resource
import retrofit2.Response

object NetworkManager {
    suspend fun <T> apiCall(apiRequest: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = apiRequest()
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        Resource.success(it)
                    } ?: Resource.error("Response body was null")
                }
                response.errorBody() != null -> {
                    Resource.error(response.message())
                }
                else -> Resource.error("Unknown error")
            }
        } catch (e: Exception) {
            Resource.error(e.message)
        }
    }
}