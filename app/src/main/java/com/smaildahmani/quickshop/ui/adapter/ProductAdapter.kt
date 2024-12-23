package com.smaildahmani.quickshop.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.model.Product
import com.smaildahmani.quickshop.ui.viewholder.ProductViewHolder
import com.smaildahmani.quickshop.util.BASE_URL

class ProductAdapter(
    private val products: List<Product>,
    private val onAddToCartClicked: (Product) -> Unit
) : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // Load image using Glide
        val imageUrl = "$BASE_URL/images/${product.image}"

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image) // Add a placeholder image in `res/drawable`
            .error(R.drawable.error_image)            // Add an error image in `res/drawable`
            .into(holder.productImage)

        // Set product details
        holder.productName.text = product.name
        holder.productPrice.text = "Price: ${product.price} USD"
        holder.productCategory.text = "Category: ${product.category}"

        // Handle "Add to Cart" button click
        holder.addToCartButton.setOnClickListener {
            onAddToCartClicked(product)
        }
    }

    override fun getItemCount(): Int = products.size
}
