package com.smaildahmani.quickshop.util

import com.smaildahmani.quickshop.api.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val BASE_URL = "http://10.0.2.2:8080"


fun <T> handleApiResponse(
    call: Call<ApiResponse<T>>,
    onSuccess: (T) -> Unit,
    onError: (String) -> Unit
) {
    call.enqueue(object : Callback<ApiResponse<T>> {
        override fun onResponse(call: Call<ApiResponse<T>>, response: Response<ApiResponse<T>>) {
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.data != null) {
                    onSuccess(body.data)
                } else {
                    onError(body?.error ?: "Unknown error")
                }
            } else {
                onError("Error: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<ApiResponse<T>>, t: Throwable) {
            onError("Error failure: ${t.message}")
        }
    })
}
