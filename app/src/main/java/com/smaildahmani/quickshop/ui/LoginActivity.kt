package com.smaildahmani.quickshop.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.api.ApiClient
import com.smaildahmani.quickshop.api.LoginRequest
import com.smaildahmani.quickshop.api.UserResponse
import com.smaildahmani.quickshop.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(isUserLoggedIn()){
            startActivity(Intent(this, MainActivity::class.java));
            finish();
        }

        // Initialize UI components
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        // Set Login button click listener
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            } else {
                performLogin(email, password)
            }
        }

        // Set Register link click listener
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        val apiService = ApiClient.getApiService(this)

        val call = apiService.login(loginRequest)

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val user =  response.body()?.data
                    System.out.println(user)
                    saveCredentials(email, password, user!!)
                    Toast.makeText(
                        this@LoginActivity,
                        "Welcome, ${user?.firstName}!",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Redirect to MainActivity
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Invalid email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun saveCredentials(email: String, password: String, user: User) {
        val sharedPref = getSharedPreferences("QuickShop", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("EMAIL", email)
            putString("PASSWORD", password)
            putString("firstName", user.firstName)
            putString("lastName", user.lastName)
            putString("phone", user.phone)
            putString("address", user.address)
            putString("ROLE", user.role)
            apply()
        }
    }


    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("QuickShop", MODE_PRIVATE)
        val email = sharedPref.getString("EMAIL", null)
        val password = sharedPref.getString("PASSWORD", null)
        return email != null && password != null
    }
}
