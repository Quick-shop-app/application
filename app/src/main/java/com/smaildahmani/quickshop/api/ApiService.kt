package com.smaildahmani.quickshop.api

import com.smaildahmani.quickshop.model.CartItem
import com.smaildahmani.quickshop.model.Product
import com.smaildahmani.quickshop.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Login
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<UserResponse>

    // Register
    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<GenericResponse>

    @GET("api/cart")
    fun getCart(@Query("username") username: String): Call<List<CartItem>>

    @GET("products")
    fun getProducts(): Call<ApiResponse<List<Product>>>
}

// Data Models
data class LoginRequest(val email: String, val password: String)
data class UserResponse(val data: User, val success: Boolean)
data class RegisterRequest(val firstName: String, val lastName: String, val email: String, val password: String, val confirmPassword: String)
data class GenericResponse(val success: Boolean, val message: String)