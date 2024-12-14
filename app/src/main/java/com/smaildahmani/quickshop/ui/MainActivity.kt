package com.smaildahmani.quickshop.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiService
import com.smaildahmani.quickshop.api.RetrofitClient
import com.smaildahmani.quickshop.ui.adapter.ProductAdapter
import com.smaildahmani.quickshop.util.BASE_URL
import com.smaildahmani.quickshop.util.handleApiResponse


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main) // Link to the XML layout


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val call = RetrofitClient.retrofit.create(ApiService::class.java).getProducts()

        handleApiResponse(
            call = call,
            onSuccess = { products ->
                val adapter = ProductAdapter(
                    products = products,
                    onAddToCartClicked = { product ->
                        // Handle "Add to Cart" button click
                        Toast.makeText(
                            this,
                            "${product.name} added to cart!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Add your cart logic here (e.g., save to local database or send to API)
                    }
                )
                recyclerView.adapter = adapter
            },
            onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }
}
