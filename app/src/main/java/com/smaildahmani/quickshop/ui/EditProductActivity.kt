package com.smaildahmani.quickshop.ui

import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiClient
import com.smaildahmani.quickshop.api.ApiResponse
import com.smaildahmani.quickshop.api.ApiService
import com.smaildahmani.quickshop.model.Product
import com.smaildahmani.quickshop.util.BASE_URL
import com.smaildahmani.quickshop.util.FileUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProductActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var getImageLauncher: ActivityResultLauncher<String>
    private var selectedImageUri: Uri? = null
    private var productId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        apiService = ApiClient.getApiService(this)

        // Initialize views
        val etProductName: EditText = findViewById(R.id.etProductName)
        val etProductBrand: EditText = findViewById(R.id.etProductBrand)
        val etProductCategory: EditText = findViewById(R.id.etProductCategory)
        val etProductPrice: EditText = findViewById(R.id.etProductPrice)
        val etProductDescription: EditText = findViewById(R.id.etProductDescription)
        val ivCurrentProductImage: ImageView = findViewById(R.id.ivCurrentProductImage)
        val btnSelectNewImage: Button = findViewById(R.id.btnSelectNewImage)
        val ivNewProductImagePreview: ImageView = findViewById(R.id.ivNewProductImagePreview)
        val btnUpdateProduct: Button = findViewById(R.id.btnUpdateProduct)

        // Get product ID from intent
        productId = intent.getLongExtra("PRODUCT_ID", 0)

        // Fetch product details
        fetchProductDetails(
            productId, etProductName, etProductBrand, etProductCategory,
            etProductPrice, etProductDescription, ivCurrentProductImage
        )

        // Initialize ActivityResultLauncher
        getImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                ivNewProductImagePreview.apply {
                    visibility = ImageView.VISIBLE
                    setImageURI(uri)
                }
            }
        }

        // Select new image
        btnSelectNewImage.setOnClickListener {
            getImageLauncher.launch("image/*")
        }

        // Update product
        btnUpdateProduct.setOnClickListener {
            val name = etProductName.text.toString()
            val brand = etProductBrand.text.toString()
            val category = etProductCategory.text.toString()
            val price = etProductPrice.text.toString()
            val description = etProductDescription.text.toString()

            if (name.isEmpty() || brand.isEmpty() || category.isEmpty() || price.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            updateProduct(productId, name, brand, category, price, description, selectedImageUri)
        }
    }

    private fun fetchProductDetails(
        productId: Long,
        etName: EditText,
        etBrand: EditText,
        etCategory: EditText,
        etPrice: EditText,
        etDescription: EditText,
        ivPreview: ImageView
    ) {
        val call = apiService.getProductById(productId)
        call.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        etName.setText(product.name)
                        etBrand.setText(product.brand)
                        etCategory.setText(product.category)
                        etPrice.setText(product.price.toString())
                        etDescription.setText(product.description)

                        val imageUrl = "${BASE_URL}/images/${product.image}"
                        Glide.with(this@EditProductActivity)
                            .load(imageUrl)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.error_image)
                            .into(ivPreview)
                    }
                } else {
                    Toast.makeText(this@EditProductActivity, "Failed to fetch product details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@EditProductActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()

            }
        })
    }


    private fun updateProduct(
        productId: Long,
        name: String,
        brand: String,
        category: String,
        price: String,
        description: String,
        imageUri: Uri?
    ) {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val brandPart = brand.toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryPart = category.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())

        // Prepare image file if a new one is selected
        val imagePart = imageUri?.let {
            val file = FileUtils.getFileFromUri(this, it)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imageFile", file.name, requestFile)
        }

        // Call API to update product
        apiService.editProduct(
            productId = productId,
            name = namePart,
            brand = brandPart,
            category = categoryPart,
            price = pricePart,
            description = descriptionPart,
            imageFile = imagePart
        ).enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(call: Call<ApiResponse<Void>>, response: Response<ApiResponse<Void>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditProductActivity, "Product updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditProductActivity, "Failed to update product: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                Toast.makeText(this@EditProductActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
