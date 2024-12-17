package com.smaildahmani.quickshop.util

import com.smaildahmani.quickshop.model.CartItem
import com.smaildahmani.quickshop.model.Product

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    /**
     * Add a product to the cart. If the product already exists, update its quantity.
     */
    fun addProduct(product: Product, quantity: Int) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
            existingItem.totalPrice = existingItem.quantity * product.price
        } else {
            cartItems.add(
                CartItem(
                    id = cartItems.size + 1, // Assign a temporary ID for local cart
                    product = product,
                    quantity = quantity,
                    totalPrice = quantity * product.price
                )
            )
        }
    }

    /**
     * Remove a product from the cart based on its product ID.
     */
    fun removeProduct(productId: Int) {
        cartItems.removeAll { it.product.id == productId }
    }

    /**
     * Clear all items from the cart.
     */
    fun clearCart() {
        cartItems.clear()
    }

    /**
     * Get all items in the cart.
     */
    fun getCartItems(): List<CartItem> {
        return cartItems
    }

    /**
     * Get the total price of all items in the cart.
     */
    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.totalPrice }
    }
}
