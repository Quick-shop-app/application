package com.smaildahmani.quickshop.model

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String?,
    val role: String,
    val createdAt: String
)
