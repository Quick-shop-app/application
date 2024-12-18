package com.smaildahmani.quickshop.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import com.smaildahmani.quickshop.LoginActivity
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiClient
import com.smaildahmani.quickshop.api.ApiService
import com.smaildahmani.quickshop.ui.adapter.CartAdapter
import com.smaildahmani.quickshop.util.handleApiResponse

class CartActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView = findViewById(R.id.cartRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        if(!isUserLoggedIn()){
            startActivity(Intent(this, LoginActivity::class.java));
            finish()
        }

        loadCartItems()

        swipeRefreshLayout.setOnRefreshListener {
            loadCartItems()
        }


        // Set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back button

    }

    private fun loadCartItems() {
        val apiService = ApiClient.getApiService(this)
        val call = apiService.getCart()

        handleApiResponse(
            call = call,
            onSuccess = { cartItems ->
                val adapter = CartAdapter(
                    cartItems = cartItems,
                    onRemoveClicked = { productId ->
                        removeItemFromCart(productId.toLong(), apiService)
                    }
                )

                recyclerView.adapter = adapter
                swipeRefreshLayout.isRefreshing = false
            },
            onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false
            }
        )

    }

    private fun removeItemFromCart(productId: Long, apiService: ApiService){
        val call = apiService.removeProduct(productId)
        swipeRefreshLayout.isRefreshing = true
        handleApiResponse(
            call = call,
            onSuccess = {
                Toast.makeText(this, "Removed product $productId from cart!", Toast.LENGTH_SHORT)
                    .show()
                loadCartItems()
            },
            onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // Handle back button click
                finish() // Navigate back to MainActivity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
