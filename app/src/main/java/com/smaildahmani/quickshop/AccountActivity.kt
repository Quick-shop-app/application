package com.smaildahmani.quickshop

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
import com.smaildahmani.quickshop.ui.CartActivity
import com.smaildahmani.quickshop.ui.MainActivity

class AccountActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

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

        // Retrieve user data from SharedPreferences
        val sharedPref: SharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)
        val firstName = sharedPref.getString("firstName", "N/A")
        val lastName = sharedPref.getString("lastName", "N/A")
        val email = sharedPref.getString("EMAIL", "N/A")
        val phone = sharedPref.getString("phone", "N/A")
        val address = sharedPref.getString("address", "N/A")

        // Set user info in UI
        tvUserFullName.text = "$firstName $lastName"
        tvUserEmail.text = email
        tvUserPhone.text = phone
        tvUserAddress.text = address

        // Logout button functionality
        btnLogout.setOnClickListener {
            // Clear user data from SharedPreferences
            with(sharedPref.edit()) {
                clear()
                apply()
            }

            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()

            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
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

                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.account
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE)
        val email = sharedPref.getString("EMAIL", null)
        val password = sharedPref.getString("PASSWORD", null)
        return email != null && password != null
    }
}
