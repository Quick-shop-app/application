package com.smaildahmani.quickshop.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.smaildahmani.quickshop.LoginActivity
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiClient
import com.smaildahmani.quickshop.api.ApiResponse
import com.smaildahmani.quickshop.ui.adapter.ProductAdapter
import com.smaildahmani.quickshop.util.handleApiResponse
import retrofit2.Call

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
//        val call = RetrofitClient.retrofit.create(ApiService::class.java).getProducts()
        val apiService = ApiClient.getApiService(this)
        val call = apiService.getProducts()

        handleApiResponse(
            call = call,
            onSuccess = { products ->
                val adapter = ProductAdapter(
                    products = products,
                    onAddToCartClicked = { product ->
                        if (!isUserLoggedIn()) {
                            // Redirect to Login if not authenticated
                            Toast.makeText(this, "Please login to add to cart", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        } else {
                            // Add to Cart
                            apiService.addProduct(product.id, 1).enqueue(
                                object : retrofit2.Callback<ApiResponse<Void>> {
                                    override fun onResponse(
                                        call: retrofit2.Call<ApiResponse<Void>>,
                                        response: retrofit2.Response<ApiResponse<Void>>
                                    ) {
                                        if (response.isSuccessful) {
                                            val body = response.body()
                                            if (body?.success == true) {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Product added to cart!",
                                                    Toast.LENGTH_SHORT
                                                ).show();
                                            }
                                            else {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Failed to add product to cart!",
                                                    Toast.LENGTH_SHORT
                                                ).show();
                                                }
                                        }
                                        else {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Error: ${response.code()}",
                                                Toast.LENGTH_SHORT
                                                ).show();
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<ApiResponse<Void>>,
                                        t: Throwable
                                    ) {

                                        Toast.makeText(
                                            this@MainActivity,
                                            "Error: ${t.message}",
                                            Toast.LENGTH_SHORT
                                        ).show();
                                    }

                                }

                            )

                            Toast.makeText(this, "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
                        }
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

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE)
        val email = sharedPref.getString("EMAIL", null)
        val password = sharedPref.getString("PASSWORD", null)
        return email != null && password != null
    }
}
