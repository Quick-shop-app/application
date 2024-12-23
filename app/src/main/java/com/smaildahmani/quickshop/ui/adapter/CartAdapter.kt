package com.smaildahmani.quickshop.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.model.CartItem
import com.smaildahmani.quickshop.util.BASE_URL

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onRemoveClicked: (Long) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.productName)
        val price: TextView = itemView.findViewById(R.id.productPrice)
        val quantity: TextView = itemView.findViewById(R.id.productQuantity)
        val removeButton: Button = itemView.findViewById(R.id.removeButton)
        val totalPrice: TextView = itemView.findViewById(R.id.productTotalPrice)
        val productImage: ImageView = itemView.findViewById<ImageView>(R.id.productImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val imageUrl = "$BASE_URL/images/${cartItem.product.image}"

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image) // Add a placeholder image in `res/drawable`
            .error(R.drawable.error_image)            // Add an error image in `res/drawable`
            .into(holder.productImage)


        holder.name.text = cartItem.product.name
        holder.price.text = "$${cartItem.product.price}"
        holder.quantity.text = "Qty: ${cartItem.quantity}"
        holder.totalPrice.text = "Total: $${cartItem.totalPrice}"
        holder.removeButton.setOnClickListener { onRemoveClicked(cartItem.product.id) }
    }

    fun removeItem(productId: Long) {
        val position = cartItems.indexOfFirst { it.product.id == productId }
        if (position != -1) {
            cartItems.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    override fun getItemCount(): Int = cartItems.size
}
