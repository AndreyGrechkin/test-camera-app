package com.defey.testapp.domain


sealed class Response<out T> {
    data class Success<out T>(val value: T) : Response<T>()

    data class Failure(
        val errorMessage: String?
    ) : Response<Nothing>()

    data object Loading : Response<Nothing>()
}

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Response<T> {
        return try {
            Response.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Response.Failure(
                throwable.localizedMessage
            )
        }
    }
}