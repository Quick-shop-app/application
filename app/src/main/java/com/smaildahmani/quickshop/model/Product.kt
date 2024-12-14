package com.smaildahmani.quickshop.model

data class Product(
    val id: Int,
    val brand: String,
    val category: String,
    val description: String?,
    val image: String,
    val name: String,
    val price: Double
)
