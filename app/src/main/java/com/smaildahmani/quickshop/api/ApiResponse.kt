package com.smaildahmani.quickshop.api

data class ApiResponse<T>(
    val data: T?,
    val count: Int?,
    val totalPrice: Double?,
    val error: String?,
    val success: Boolean?
)
