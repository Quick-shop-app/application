package com.smaildahmani.quickshop.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.model.CartItem

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val onRemoveClicked: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.productName)
        val price: TextView = itemView.findViewById(R.id.productPrice)
        val quantity: TextView = itemView.findViewById(R.id.productQuantity)
        val removeButton: Button = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.name.text = cartItem.product.name
        holder.price.text = "$${cartItem.product.price}"
        holder.quantity.text = "Qty: ${cartItem.quantity}"
        holder.removeButton.setOnClickListener { onRemoveClicked(cartItem.product.id.toInt()) }
    }

    override fun getItemCount(): Int = cartItems.size
}
