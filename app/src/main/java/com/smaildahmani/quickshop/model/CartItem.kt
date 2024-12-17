package com.smaildahmani.quickshop.model

data class CartItem(
    val id: Int,
    val product: Product,
    var quantity: Int,
    var totalPrice: Double
)
