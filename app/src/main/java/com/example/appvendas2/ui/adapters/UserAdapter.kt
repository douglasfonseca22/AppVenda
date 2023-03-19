package com.example.appvendas2.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appvendas2.R
import com.example.appvendas2.data.models.Client
import com.google.android.material.textview.MaterialTextView

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var list = mutableListOf<Client>()
    private var onItemClickListener: ((Client) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_item_user, parent, false)

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = list[position]
        holder.tvFirstName.text = item.client

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
    }

    override fun getItemCount() = list.size

    fun setData(data: List<Client>) {
        list.apply {
            clear()
            addAll(data)
        }
    }

    fun setOnItemClickListener(listener: (Client) -> Unit) {
        onItemClickListener = listener
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFirstName = itemView.findViewById<MaterialTextView>(R.id.tv_product)
    }
}