package com.smaildahmani.quickshop.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smaildahmani.quickshop.AccountActivity
import com.smaildahmani.quickshop.LoginActivity
import com.smaildahmani.quickshop.MapsActivity
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiClient
import com.smaildahmani.quickshop.api.ApiResponse
import com.smaildahmani.quickshop.api.ApiService
import com.smaildahmani.quickshop.model.CartItem
import com.smaildahmani.quickshop.ui.adapter.CartAdapter
import com.smaildahmani.quickshop.util.handleApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var finalizeOrderButton: Button
    private lateinit var totalPriceTextView: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var emptyCartMessage: TextView
//    cartTotalLayout
    private lateinit var cartTotalLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
        title = "Cart Items"
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView = findViewById(R.id.cartRecyclerView)
        totalPriceTextView = findViewById(R.id.totalPriceTextView)
        finalizeOrderButton = findViewById(R.id.finalizeOrderButton)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        emptyCartMessage = findViewById(R.id.emptyCartMessage)
        cartTotalLayout = findViewById(R.id.cartTotalLayout)

        recyclerView.layoutManager = LinearLayoutManager(this)

        if (!isUserLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loadCartItems()

        swipeRefreshLayout.setOnRefreshListener {
            loadCartItems()
        }

        finalizeOrderButton.setOnClickListener {
            finalizeOrder()
        }

      bottomNavigationView.setOnItemSelectedListener { item ->
          val options = ActivityOptions.makeCustomAnimation(
              this,
              android.R.anim.fade_in,
              android.R.anim.fade_out
          )

        when (item.itemId) {
            R.id.navigation_home -> {
                startActivity(Intent(this, MainActivity::class.java), options.toBundle())
                true
            }
            R.id.navigation_cart -> {
                // Already in CartActivity
                true
            }

            R.id.account -> {
                startActivity(Intent(this, AccountActivity::class.java), options.toBundle())
                true
            }
            else -> false
        }
      }
          bottomNavigationView.selectedItemId = R.id.navigation_cart

    }

    private fun loadCartItems() {
        val apiService = ApiClient.getApiService(this)
        val call = apiService.getCart()

        call.enqueue(
            object : Callback<ApiResponse<List<CartItem>>> {
                override fun onResponse(
                    call: Call<ApiResponse<List<CartItem>>>,
                    response: Response<ApiResponse<List<CartItem>>>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        val cartItems = apiResponse?.data ?: emptyList()
                        val totalPrice = apiResponse?.totalPrice ?: 0.0

                        if (cartItems.isEmpty()) {
                            emptyCartMessage.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                            cartTotalLayout.visibility = View.GONE
                        } else {
                            emptyCartMessage.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            cartTotalLayout.visibility = View.VISIBLE
                        }

                        val adapter = CartAdapter(
                            cartItems = cartItems.toMutableList(),
                            onRemoveClicked = { productId ->
                                removeItemFromCart(productId, apiService)
                            }
                        )
                        recyclerView.adapter = adapter
                        totalPriceTextView.text = "Total: $$totalPrice"
                        swipeRefreshLayout.isRefreshing = false
                    } else {
                        Toast.makeText(
                            this@CartActivity,
                            "Failed to load cart items",
                            Toast.LENGTH_SHORT
                        ).show()
                        swipeRefreshLayout.isRefreshing = false
                    }
                }

                override fun onFailure(call: Call<ApiResponse<List<CartItem>>>, t: Throwable) {
                    Toast.makeText(
                        this@CartActivity,
                        "Failed to load cart items",
                        Toast.LENGTH_SHORT
                    ).show()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        )
    }

    private fun finalizeOrder() {
        val apiService = ApiClient.getApiService(this)
        val call = apiService.finalizeCart()

        swipeRefreshLayout.isRefreshing = true
        handleApiResponse(
            call = call,
            onSuccess = {
                Toast.makeText(this, "Order finalized successfully!", Toast.LENGTH_SHORT).show()
                loadCartItems()
            },
            onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false
            }
        )
    }

 private fun removeItemFromCart(productId: Long, apiService: ApiService) {
    val call = apiService.removeProduct(productId)
    swipeRefreshLayout.isRefreshing = true
     call.enqueue(
        object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>,
                response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        Toast.makeText(this@CartActivity, "Removed product from cart!", Toast.LENGTH_SHORT).show()
                        // Update the adapter's data and notify the change
                        val adapter = recyclerView.adapter as CartAdapter
                        adapter.removeItem(productId)
                        swipeRefreshLayout.isRefreshing = false
                    } else {
                        Toast.makeText(this@CartActivity, "Failed to remove product from cart!", Toast.LENGTH_SHORT).show()
                        swipeRefreshLayout.isRefreshing = false
                    }
                    loadCartItems()
                } else {
                    Toast.makeText(this@CartActivity, "Failed to remove product from cart!", Toast.LENGTH_SHORT).show()
                    swipeRefreshLayout.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Failed to remove product from cart!", Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false
            }
        }

    )
}

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE)
        val email = sharedPref.getString("EMAIL", null)
        val password = sharedPref.getString("PASSWORD", null)
        return email != null && password != null
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
