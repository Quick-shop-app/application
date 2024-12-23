package com.smaildahmani.quickshop.ui.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smaildahmani.quickshop.R

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val productImage: ImageView = itemView.findViewById(R.id.productImage)
    val productName: TextView = itemView.findViewById(R.id.productName)
    val productPrice: TextView = itemView.findViewById(R.id.productPrice)
    val productCategory: TextView = itemView.findViewById(R.id.productCategory)
    val addToCartButton: Button = itemView.findViewById(R.id.addToCartButton)
}
