package com.smaildahmani.quickshop.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiClient
import com.smaildahmani.quickshop.api.ApiResponse
import com.smaildahmani.quickshop.api.ApiService
import com.smaildahmani.quickshop.util.FileUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var getImageLauncher: ActivityResultLauncher<String>
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_product)

        apiService = ApiClient.getApiService(this)

        val etProductName: EditText = findViewById(R.id.etProductName)
        val etProductBrand: EditText = findViewById(R.id.etProductBrand)
        val etProductCategory: EditText = findViewById(R.id.etProductCategory)
        val etProductPrice: EditText = findViewById(R.id.etProductPrice)
        val etProductDescription: EditText = findViewById(R.id.etProductDescription)
        val btnSelectImage: Button = findViewById(R.id.btnSelectImage)
        val ivProductImagePreview: ImageView = findViewById(R.id.ivProductImagePreview)
        val btnSubmitProduct: Button = findViewById(R.id.btnSubmitProduct)

        // Initialize ActivityResultLauncher
        getImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                ivProductImagePreview.apply {
                    visibility = ImageView.VISIBLE
                    setImageURI(uri)
                }
            }
        }

        // enable toolbar back button
        title = "Add Product"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        btnSelectImage.setOnClickListener {
            getImageLauncher.launch("image/*") // Launch image picker for images only
        }

        btnSubmitProduct.setOnClickListener {
            val name = etProductName.text.toString()
            val brand = etProductBrand.text.toString()
            val category = etProductCategory.text.toString()
            val price = etProductPrice.text.toString()
            val description = etProductDescription.text.toString()

            if (name.isEmpty() || brand.isEmpty() || category.isEmpty() || price.isEmpty() || description.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createProduct(name, brand, category, price, description, selectedImageUri!!)
        }
    }

    private fun createProduct(name: String, brand: String, category: String, price: String, description: String, imageUri: Uri) {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val brandPart = brand.toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryPart = category.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())

        // Convert URI to File
        val file = FileUtils.getFileFromUri(this, imageUri)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("imageFile", file.name, requestFile)

        // Call API to create product
        apiService.createProduct(namePart, brandPart, categoryPart, pricePart, descriptionPart, imagePart)
            .enqueue(object : Callback<ApiResponse<Void>> {
                override fun onResponse(call: Call<ApiResponse<Void>>, response: Response<ApiResponse<Void>>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddProductActivity, "Product created successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddProductActivity, "Failed to create product: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                    Toast.makeText(this@AddProductActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
