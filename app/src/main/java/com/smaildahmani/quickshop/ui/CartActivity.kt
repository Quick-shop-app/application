package com.smaildahmani.quickshop.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiService
import com.smaildahmani.quickshop.api.RetrofitClient
import com.smaildahmani.quickshop.model.CartItem
import com.smaildahmani.quickshop.ui.adapter.CartAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private val username = "admin@admin.com" // Replace with the actual user email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadCartItems()
    }

    private fun loadCartItems() {
//        RetrofitClient.retrofit.create(ApiService::class.java).getCart(username).enqueue(object : Callback<List<CartItem>> {
//            override fun onResponse(
//                call: Call<List<CartItem>>,
//                response: Response<List<CartItem>>
//            ) {
//                if (response.isSuccessful) {
//                    val cartItems = response.body() ?: emptyList()
//                    adapter = CartAdapter(cartItems) { productId ->
//                        removeItemFromCart(productId)
//                    }
//                    recyclerView.adapter = adapter
//                } else {
//                    Toast.makeText(this@CartActivity, "Failed to load cart!", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//
//            override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
//                Toast.makeText(this@CartActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
    }

    private fun removeItemFromCart(productId: Int) {
        // Implement API call to remove item
        Toast.makeText(this, "Removed product $productId from cart!", Toast.LENGTH_SHORT).show()
    }
}
