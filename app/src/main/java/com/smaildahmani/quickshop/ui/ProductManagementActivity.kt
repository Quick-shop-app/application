package com.smaildahmani.quickshop.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiClient
import com.smaildahmani.quickshop.api.ApiResponse
import com.smaildahmani.quickshop.api.ApiService
import com.smaildahmani.quickshop.model.Product
import com.smaildahmani.quickshop.ui.adapter.AdminProductAdapter
import com.smaildahmani.quickshop.util.handleApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductManagementActivity : AppCompatActivity() {

    private lateinit var rvProductList: RecyclerView
    private lateinit var fabCreateProduct: FloatingActionButton
    private lateinit var adminProductAdapter: AdminProductAdapter
    private var productList: List<Product> = listOf()

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_management)

        rvProductList = findViewById(R.id.rvProductList)
        fabCreateProduct = findViewById(R.id.fabCreateProduct)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        rvProductList.layoutManager = LinearLayoutManager(this)

        fabCreateProduct.setOnClickListener {
//            val intent = Intent(this, AddProductActivity::class.java)
//            startActivity(intent)
        }

        val apiService = ApiClient.getApiService(this)
        loadProducts(apiService)
    }

    private fun loadProducts(apiService: ApiService) {

        val call = apiService.getProducts()

        handleApiResponse(
            call = call,
            onSuccess = { products ->
                    productList = products ?: listOf()
                    adminProductAdapter = AdminProductAdapter(
                        context = this@ProductManagementActivity,
                        products = productList,
                        onEditClicked = { product ->
//                            val intent = Intent(this@com.smaildahmani.quickshop.ui.ProductManagementActivity, EditProductActivity::class.java)
//                            intent.putExtra("PRODUCT_ID", product.id)
//                            startActivity(intent)
                        },
                        onDeleteClicked = { product ->
                            deleteProduct(product, apiService)
                        }
                    )
                    rvProductList.adapter = adminProductAdapter
            },
            onError = {
                Toast.makeText(
                    this@ProductManagementActivity,
                    "Failed to load products",
                    Toast.LENGTH_SHORT
                ).show()

        })

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
                    startActivity(Intent(this, CartActivity::class.java), options.toBundle())
                    true
                }

                R.id.account -> {
                    startActivity(Intent(this, AccountActivity::class.java), options.toBundle())
                    true
                }

                R.id.navigation_product_management -> {
                    true
                }

                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_product_management
    }

    private fun deleteProduct(product: Product, apiService: ApiService) {
        val call = apiService.removeProduct(product.id)

        call.enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(call: Call<ApiResponse<Void>>, response: Response<ApiResponse<Void>>) {
                if (response.isSuccessful) {
                        Toast.makeText(this@ProductManagementActivity, "Product deleted!", Toast.LENGTH_SHORT).show()
                        loadProducts(apiService)
                } else {
                    Toast.makeText(this@ProductManagementActivity, "Failed to delete product!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                Toast.makeText(this@ProductManagementActivity, "Failed to delete product!", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
