package com.smaildahmani.quickshop.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import com.smaildahmani.quickshop.LoginActivity
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiClient
import com.smaildahmani.quickshop.api.ApiResponse
import com.smaildahmani.quickshop.ui.adapter.ProductAdapter
import com.smaildahmani.quickshop.util.handleApiResponse
import retrofit2.Call

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView = findViewById(R.id.recyclerView)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadProducts()

        // Setup Swipe-to-Refresh
        swipeRefreshLayout.setOnRefreshListener {
            loadProducts() // Reload data on swipe
        }

        // Setup Hamburger Menu
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle Navigation Item Clicks
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_view_cart -> {
                    // Open Cart Activity
                    startActivity(Intent(this, CartActivity::class.java))
                }
                R.id.nav_logout -> {
                    // Handle Logout
                    logoutUser()
                }
            }
            drawerLayout.closeDrawers()
            true
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

    private fun logoutUser() {
        // Clear stored credentials
        val sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
