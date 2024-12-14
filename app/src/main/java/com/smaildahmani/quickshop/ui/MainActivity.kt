package com.smaildahmani.quickshop.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiService
import com.smaildahmani.quickshop.api.RetrofitClient
import com.smaildahmani.quickshop.ui.adapter.ProductAdapter
import com.smaildahmani.quickshop.util.handleApiResponse

class MainActivity : ComponentActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load initial data
        loadProducts()

        // Setup Swipe-to-Refresh
        swipeRefreshLayout.setOnRefreshListener {
            loadProducts() // Reload data on swipe
        }
    }

    private fun loadProducts() {
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
                        // Add cart logic here
                    }
                )
                recyclerView.adapter = adapter

                // Stop Swipe-to-Refresh animation
                swipeRefreshLayout.isRefreshing = false
            },
            onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                // Stop Swipe-to-Refresh animation on error
                swipeRefreshLayout.isRefreshing = false
            }
        )
    }
}
