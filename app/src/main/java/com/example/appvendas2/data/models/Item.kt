package com.example.appvendas2.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "client")
data class Client(
    val client: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity(tableName = "item")
data class Item(
    val description: String,
    val quantity: Int,
    val unitPrice: Double,
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun getTotalValue(): Double {
        return quantity * unitPrice
    }
}


@Entity(tableName = "orders")
data class OrderEntity(
    val items: List<Item>,
    val totalValue: Double
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


