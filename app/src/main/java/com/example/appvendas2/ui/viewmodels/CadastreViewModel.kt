package com.example.appvendas2.ui.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.appvendas2.data.models.Item

class CadastreViewModel(application: Application) : AndroidViewModel(application) {

    private val _items = mutableListOf<Item>()
    val items: List<Item>
        get() = _items

    val totalValue: Double
        get() = calculateTotalValue()

    val totalQuantity: Int
        get() = calculateTotalQuantity(items)

    fun addItem(description: String, quantity: Int?, unitPrice: Double?) {
        if (description.isNotEmpty() && quantity != null && quantity > 0 && unitPrice != null && unitPrice > 0) {
            val item = Item(description, quantity, unitPrice)
            _items.add(item)
        } else {
            Toast.makeText(
                getApplication(),
                "Preencha todos os campos corretamente",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun calculateTotalValue(): Double {
        var totalValue = 0.0
        for (item in _items) {
            totalValue += item.quantity * item.unitPrice
        }
        return totalValue
    }

    fun calculateTotalQuantity(items: List<Item>): Int {
        var totalQuantity = 0
        for (item in items) {
            totalQuantity += item.quantity
        }
        return totalQuantity
    }

    fun calculateTotalValueList(items: List<Item>): Double {
        var totalValue = 0.0
        for (item in items) {
            totalValue += item.quantity * item.unitPrice
        }
        return totalValue
    }

}
