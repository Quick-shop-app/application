package com.smaildahmani.quickshop.model

data class CartItem(
    val id: Long,
    val product: Product,
    var quantity: Long,
    var totalPrice: Double
)
