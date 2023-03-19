package com.example.appvendas2.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.appvendas2.R
import com.example.appvendas2.data.models.Item
import java.util.*

class ItemListAdapter(context: Context, private val itemList: MutableList<Item>) :
    BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return itemList[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = inflater.inflate(R.layout.item_card_list, parent, false)
            holder = ViewHolder()
            holder.description = view.findViewById(R.id.tv_product)
            holder.quantity = view.findViewById(R.id.tv_quantity)
            holder.unitPrice = view.findViewById(R.id.tv_unit_price)
            holder.totalPrice = view.findViewById(R.id.tv_total_price)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val item = itemList[position]

        holder.description?.text = item.description
        holder.quantity?.text = item.quantity.toString()
        holder.unitPrice?.text = String.format(Locale.getDefault(), "R$ %.2f", item.unitPrice)
        holder.totalPrice?.text = String.format(Locale.getDefault(), "R$ %.2f", item.getTotalValue())

        return view!!
    }

    private class ViewHolder {
        var description: TextView? = null
        var quantity: TextView? = null
        var unitPrice: TextView? = null
        var totalPrice: TextView? = null
    }
}