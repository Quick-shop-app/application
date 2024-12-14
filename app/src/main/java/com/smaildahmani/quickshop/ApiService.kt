package com.smaildahmani.quickshop

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    fun getProducts(): Call<List<Product>>
}