package com.smaildahmani.quickshop.api

data class ApiResponse<T>(
    val data: T?,
    val count: Int?,
    val error: String?,
    val success: Boolean?
)
