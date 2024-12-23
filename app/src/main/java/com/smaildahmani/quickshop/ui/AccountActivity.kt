package com.smaildahmani.quickshop.ui

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smaildahmani.quickshop.R

class AccountActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account)

        // UI References
        val tvUserFullName: TextView = findViewById(R.id.tvUserFullName)
        val tvUserEmail: TextView = findViewById(R.id.tvUserEmail)
        val tvUserPhone: TextView = findViewById(R.id.tvUserPhone)
        val tvUserAddress: TextView = findViewById(R.id.tvUserAddress)
        val btnLogout: Button = findViewById(R.id.btnLogout)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        if (!isUserLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Hide admin-only option for normal users
        if (!isAdmin()) {
            val menu = bottomNavigationView.menu
            menu.removeItem(R.id.navigation_product_management)
        }

        // Retrieve user data from SharedPreferences
        val sharedPref: SharedPreferences = getSharedPreferences("QuickShop", MODE_PRIVATE)
        val firstName = sharedPref.getString("firstName", "N/A")
        val lastName = sharedPref.getString("lastName", "N/A")
        val email = sharedPref.getString("EMAIL", "N/A")
        val phone = sharedPref.getString("phone", "N/A")
        val address = sharedPref.getString("address", "N/A")
        val isAdmin = sharedPref.getString("ROLE", "USER") == "ADMIN"


        // Set user info in UI
        tvUserFullName.text = "$firstName $lastName" + if (isAdmin) " (Admin)" else ""
        tvUserEmail.text = email
        tvUserPhone.text = phone
        tvUserAddress.text = address

        // Logout button functionality
        btnLogout.setOnClickListener {
            logoutUser()
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
                    startActivity(Intent(this, CartActivity::class.java), options.toBundle())
                    true
                }

                R.id.account -> {
                    // Already in CartActivity
                    true
                }

                R.id.navigation_product_management -> {
                    if (isAdmin()) {
                        val intent = Intent(this, ProductManagementActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Access Denied: Admins Only", Toast.LENGTH_SHORT).show()
                    }
                    true
                }

                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.account
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("QuickShop", MODE_PRIVATE)
        val email = sharedPref.getString("EMAIL", null)
        val password = sharedPref.getString("PASSWORD", null)
        return email != null && password != null
    }


    private fun isAdmin(): Boolean {
        val sharedPref = getSharedPreferences("QuickShop", MODE_PRIVATE)
        return sharedPref.getString("ROLE", "USER") == "ADMIN"
    }

    private fun logoutUser() {
        // Clear stored credentials
        val sharedPref = getSharedPreferences("QuickShop", MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
