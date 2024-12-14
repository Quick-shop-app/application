package com.smaildahmani.quickshop

data class Product(
    val id: Int,
    val brand: String,
    val category: String,
    val description: String?,
    val image: String,
    val name: String,
    val price: Double
)

/*
    "id": 3,
            "name": "Laptop",
            "brand": "ROG",
            "category": "Electronics",
            "price": 20000.0,
            "description": null,
            "createdAt": "2024-11-27T18:58:18.000+00:00",
            "updatedAt": "2024-12-04T18:19:18.000+00:00",
            "image": "1733336287813_product_pexels-aheworks-691810.jpg"
        },

 */
