package com.smaildahmani.quickshop.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.smaildahmani.quickshop.LoginActivity
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiClient
import com.smaildahmani.quickshop.api.ApiService
import com.smaildahmani.quickshop.api.RegisterRequest
import com.smaildahmani.quickshop.api.RetrofitClient
import com.smaildahmani.quickshop.util.handleApiResponse

class RegisterActivity : ComponentActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize UI components
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val firstName = etFirstName.text.toString()
        val lastName = etLastName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

//        val request = RegisterRequest(firstName, lastName, email, password, confirmPassword)
//        val call = RetrofitClient.retrofit.create(ApiService::class.java).getProducts()
//
//        handleApiResponse(
//            call = call,
//            onSuccess = { response ->
//                Toast.makeText(this, "Registration Successful! Please login.", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
//            },
//            onError = { errorMessage ->
//                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//        )
    }
}
