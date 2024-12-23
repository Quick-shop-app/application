package com.smaildahmani.quickshop.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.model.Product
import com.smaildahmani.quickshop.util.BASE_URL

class AdminProductAdapter(
    private val context: Context,
    private val products: List<Product>,
    private val onEditClicked: (Product) -> Unit,
    private val onDeleteClicked: (Product) -> Unit
) : RecyclerView.Adapter<AdminProductAdapter.AdminProductViewHolder>() {

    inner class AdminProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.productName)
        val productBrand: TextView = view.findViewById(R.id.productBrand)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_admin_product, parent, false)
        return AdminProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminProductViewHolder, position: Int) {
        val product = products[position]

        // Set product details
        holder.productName.text = product.name
        holder.productBrand.text = "Brand: ${product.brand}"
        holder.productPrice.text = "Price: ${product.price} USD"

        // Set edit and delete click listeners
        holder.btnEdit.setOnClickListener {
            onEditClicked(product)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClicked(product)
        }
    }

    override fun getItemCount(): Int = products.size
}
