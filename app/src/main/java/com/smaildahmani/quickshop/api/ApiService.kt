package com.smaildahmani.quickshop.api

import com.smaildahmani.quickshop.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    fun getProducts(): Call<ApiResponse<List<Product>>>

    @GET("images/{image}")
    fun getImage(@Path("image") image: String): Call<String>
}
