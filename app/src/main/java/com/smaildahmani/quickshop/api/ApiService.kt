package com.smaildahmani.quickshop.api

import com.smaildahmani.quickshop.model.CartItem
import com.smaildahmani.quickshop.model.Product
import com.smaildahmani.quickshop.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Product routes
    @GET("products")
    fun getProducts(): Call<ApiResponse<List<Product>>>

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<UserResponse>

    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<ApiResponse<Void>>

    // User routes
    @GET("cart")
    fun getCart(): Call<ApiResponse<List<CartItem>>>

    @POST("cart/add")
    fun addProduct(@Query("productId") productId: Long, @Query("quantity") quantity: Int): Call<ApiResponse<Void>>

    @POST("cart/remove")
    fun removeProduct(@Query("productId") productId: Long): Call<ApiResponse<Void>>

    @POST("cart/finalize")
    fun finalizeCart(): Call<ApiResponse<Void>>

    // admin routes
    @GET("admin/products/{id}")
    fun getProductById(@Path("id") productId: Long): Call<Product>

    @Multipart
    @POST("admin/products")
    fun createProduct(
        @Part("name") name: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("category") category: RequestBody,
        @Part("price") price: RequestBody,
        @Part("description") description: RequestBody,
        @Part imageFile: MultipartBody.Part
    ): Call<ApiResponse<Void>>

    @Multipart
    @PUT("admin/products/{id}")
    fun editProduct(
        @Path("id") productId: Long,
        @Part("name") name: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("category") category: RequestBody,
        @Part("price") price: RequestBody,
        @Part("description") description: RequestBody,
        @Part imageFile: MultipartBody.Part?
    ): Call<ApiResponse<Void>>

    @DELETE("admin/products/{id}")
    fun deleteProduct(@Path("id") productId: Long): Call<ApiResponse<Void>>
}

// Data Models
data class LoginRequest(val email: String, val password: String)
data class UserResponse(val data: User, val success: Boolean)
data class RegisterRequest(val firstName: String, val lastName: String, val email: String, val password: String, val confirmPassword: String, val phone: String, val address: String)
